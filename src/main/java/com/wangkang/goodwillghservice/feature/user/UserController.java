package com.wangkang.goodwillghservice.feature.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final InvitationService invitationService;

    public UserController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/invitation/manager")
    @PreAuthorize("hasAnyAuthority('INVITE_MANAGER')")
    public ResponseEntity<Object> generateInviteCode4Manager() {
        Invitation invitation = invitationService.generateInvitation4Manager();
        return ResponseEntity.ok(invitation);
    }

}
