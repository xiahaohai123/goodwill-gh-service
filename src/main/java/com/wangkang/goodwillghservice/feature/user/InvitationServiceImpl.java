package com.wangkang.goodwillghservice.feature.user;

import com.wangkang.goodwillghservice.audit.entity.ActionType;
import com.wangkang.goodwillghservice.audit.entity.Auditable;
import com.wangkang.goodwillghservice.core.RedisService;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final RedisService redisService;
    @Value("${invitation.valid.days:7}")
    private Integer invitationDefaultValidDays = 7;

    private final ValueOperations<String, Object> redisOps4Value;

    @Autowired
    public InvitationServiceImpl(RedisTemplate<String, Object> redisTemplate, RedisService redisService) {
        redisOps4Value = redisTemplate.opsForValue();
        this.redisService = redisService;
    }

    /**
     * 创建邀请码，并存储到 redis
     * @return 邀请码
     */
    @Auditable(actionType = ActionType.USER, actionName = "Generate invitation for manager")
    @Override
    public Invitation generateInvitation4Manager() {
        String invitationCode = generateCode();
        Invitation invitation = new Invitation();
        invitation.setCode(invitationCode);
        invitation.setRole(BuiltInPermissionGroup.MANAGER);
        invitation.setExpireAt(OffsetDateTime.now(ZoneOffset.UTC).plusDays(invitationDefaultValidDays));

        String key = redisService.buildKey("invitation", invitationCode);
        // 3. 存储到 Redis，设置 TTL
        redisOps4Value.set(
                key,
                invitation,
                invitationDefaultValidDays,
                TimeUnit.DAYS
        );

        return invitation;
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
