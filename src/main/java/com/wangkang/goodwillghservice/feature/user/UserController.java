package com.wangkang.goodwillghservice.feature.user;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final InvitationService invitationService;
    private final UserService userService;

    public UserController(InvitationService invitationService, UserService userService) {
        this.invitationService = invitationService;
        this.userService = userService;
    }

    @PostMapping("/invitation/manager")
    @PreAuthorize("hasAnyAuthority('INVITE_MANAGER')")
    public ResponseEntity<Object> generateInviteCode4Manager() {
        Invitation invitation = invitationService.generateInvitation4Manager();
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/invitation/dealer")
    @PreAuthorize("hasAnyAuthority('INVITE_DEALER')")
    public ResponseEntity<Object> generateInviteCode4Dealer() {
        Invitation invitation = invitationService.generateInvitation4Dealer();
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/invitation/tiler")
    @PreAuthorize("hasAnyAuthority('INVITE_TILER')")
    public ResponseEntity<Object> generateInviteCode4Tiler() {
        Invitation invitation = invitationService.generateInvitation4Tiler();
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDTO registerDTO) {
        // 一阶段，核实邀请码后注册
        String invitationCode = registerDTO.getInvitationCode();
        Invitation invitation = invitationService.validateInvitation(invitationCode);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(registerDTO, userDTO);
        BeanUtils.copyProperties(invitation, userDTO);
        UserDTO resultUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(resultUser);
        // TODO 二阶段，核实手机号后注册
    }

}
