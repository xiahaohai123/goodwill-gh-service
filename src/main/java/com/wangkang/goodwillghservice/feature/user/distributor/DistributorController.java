package com.wangkang.goodwillghservice.feature.user.distributor;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/distributor")
public class DistributorController {


    private final DistributorService distributorService;

    public DistributorController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_SELF_QUERY')")
    @GetMapping("/profile")
    public ResponseEntity<Object> selfInfo(Principal principal) {
        UUID uuid = UUID.fromString(principal.getName());
        Distributor4ManagerDTO distributorProfile = distributorService.getDistributorProfile(uuid);
        return ResponseEntity.ok(distributorProfile);
    }

    @PreAuthorize("hasAnyAuthority('TILER_SALES_MODIFY')")
    @PostMapping("/sales/tiler")
    public ResponseEntity<Object> recordSales(@Valid @RequestBody TilerSalesDTO tilerSalesDTO, Principal principal) {
        UUID uuid = UUID.fromString(principal.getName());
        int recordedQuantity = distributorService.recordTilerSale(uuid, tilerSalesDTO);
        return ResponseEntity.ok(recordedQuantity);
    }
}
