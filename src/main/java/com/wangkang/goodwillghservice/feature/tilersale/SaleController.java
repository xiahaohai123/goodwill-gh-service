package com.wangkang.goodwillghservice.feature.tilersale;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private final SaleAvailableService saleAvailableService;

    public SaleController(SaleAvailableService saleAvailableService) {
        this.saleAvailableService = saleAvailableService;
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/full/all")
    public ResponseEntity<Object> buildSnapshot4AllDistributor() {
        saleAvailableService.buildFullSnapshot4AllDistributor();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/full/{distributorId}")
    public ResponseEntity<Object> buildSnapshot4SpecificDistributor(@PathVariable String distributorId) {
        UUID uuid = UUID.fromString(distributorId);
        saleAvailableService.buildFullSnapshot4Distributor(uuid);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/incremental/all")
    public ResponseEntity<Object> buildIncrementalSnapshot4AllDistributor() {
        saleAvailableService.buildIncrementalSnapshot4AllDistributor();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/incremental/{distributorId}")
    public ResponseEntity<Object> buildIncrementalSnapshot4SpecificDistributor(@PathVariable String distributorId) {
        UUID uuid = UUID.fromString(distributorId);
        saleAvailableService.buildIncrementalSnapshot4Distributor(uuid);
        return ResponseEntity.noContent().build();
    }
}
