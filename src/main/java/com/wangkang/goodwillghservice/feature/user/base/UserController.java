package com.wangkang.goodwillghservice.feature.user.base;

import com.wangkang.goodwillghservice.feature.user.base.entity.Invitation;
import com.wangkang.goodwillghservice.feature.user.base.entity.PasswordUpdateDTO;
import com.wangkang.goodwillghservice.feature.user.base.entity.RegisterDTO;
import com.wangkang.goodwillghservice.feature.user.base.entity.UserDTO;
import com.wangkang.goodwillghservice.feature.user.base.service.InvitationService;
import com.wangkang.goodwillghservice.feature.user.base.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @PostMapping("/invitation/distributor")
    @PreAuthorize("hasAnyAuthority('INVITE_DISTRIBUTOR')")
    public ResponseEntity<Object> generateInviteCode4Distributor() {
        Invitation invitation = invitationService.generateInvitation4Distributor();
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
        userDTO.addRole(invitation.getRole());
        UserDTO resultUser = userService.registerUser(userDTO);
        invitationService.invalidInvitation(invitationCode);
        return ResponseEntity.ok(resultUser);
        // TODO 二阶段，核实手机号后注册
    }

    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto, Principal principal) {
        userService.updatePassword(dto, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invitation/profile/{invitationCode}")
    public ResponseEntity<Object> getInvitationProfile(@PathVariable String invitationCode) {
        Invitation invitation = invitationService.validateInvitation(invitationCode);
        return ResponseEntity.ok(invitation);
    }
}
