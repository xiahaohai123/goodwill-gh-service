package com.wangkang.goodwillghservice.feature.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangkang.goodwillghservice.core.RedisService;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.security.entity.CustomAuthenticationToken;
import com.wangkang.goodwillghservice.share.locale.MessageService;
import com.wangkang.goodwillghservice.share.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final RedisService redisService;
    private final MessageService messageService;
    @Value("${invitation.valid.days:7}")
    private Integer invitationDefaultValidDays = 7;

    private final ValueOperations<String, Object> redisOps4Value;

    private static final String KEY_PREFIX_INVITATION = "invitation";


    @Autowired
    public InvitationServiceImpl(RedisTemplate<String, Object> redisTemplate, RedisService redisService,
                                 MessageService messageService) {
        redisOps4Value = redisTemplate.opsForValue();
        this.redisService = redisService;
        this.messageService = messageService;
    }

    /**
     * 创建邀请码，并存储到 redis
     * @return 邀请码
     */
    @Auditable(actionType = ActionType.USER, actionName = "Generate invitation for manager")
    @Override
    public Invitation generateInvitation4Manager() {
        return generateInvitation4Role(BuiltInPermissionGroup.MANAGER);
    }

    @Auditable(actionType = ActionType.USER, actionName = "Generate invitation for distributor")
    @Override
    public Invitation generateInvitation4Distributor() {
        return generateInvitation4Role(BuiltInPermissionGroup.DISTRIBUTOR);
    }

    @Auditable(actionType = ActionType.USER, actionName = "Generate invitation for tiler")
    @Override
    public Invitation generateInvitation4Tiler() {
        return generateInvitation4Role(BuiltInPermissionGroup.TILER);
    }

    /**
     * 为某个角色创建邀请函，邀请信息会被存入 Redis，以 TTL 为期限，超时算过期
     * @param role 角色
     * @return 邀请函
     */
    private Invitation generateInvitation4Role(BuiltInPermissionGroup role) {
        String invitationCode = generateCode();
        Invitation invitation = new Invitation();
        invitation.setCode(invitationCode);
        invitation.setRole(role);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;
        UUID currentUserId = UUID.fromString(customAuthenticationToken.getPrincipal().toString());
        invitation.setInviterId(currentUserId);
        invitation.setExpireAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(invitationDefaultValidDays));

        String key = redisService.buildKey(KEY_PREFIX_INVITATION, invitationCode);
        // 3. 存储到 Redis，设置 TTL
        redisOps4Value.set(
                key,
                invitation,
                invitationDefaultValidDays,
                TimeUnit.DAYS
        );
        return invitation;
    }

    @Override
    public Invitation validateInvitation(String invitationCode) {
        if (StringUtils.isBlank(invitationCode)) {
            throw new IllegalArgumentException(messageService.getMessage("invitation.error.blank"));
        }

        String key = redisService.buildKey(KEY_PREFIX_INVITATION, invitationCode);
        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();
        Invitation invitation = objectMapper.convertValue(redisOps4Value.get(key), Invitation.class);

        if (invitation == null) {
            throw new IllegalArgumentException(messageService.getMessage("invitation.error.invalid"));
        }


        return invitation;
    }

    @Override
    public void invalidInvitation(String invitationCode) {
        String key = redisService.buildKey(KEY_PREFIX_INVITATION, invitationCode);
        redisService.delete(key);
    }

    private String generateCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {         // 10字符邀请码
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        // 加上可读分隔符 AAAAA-BBBBB
        return sb.insert(5, '-').toString();
    }
}
