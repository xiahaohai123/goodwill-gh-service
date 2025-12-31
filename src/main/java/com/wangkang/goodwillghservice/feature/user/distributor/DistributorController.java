package com.wangkang.goodwillghservice.feature.user.distributor;


import com.wangkang.goodwillghservice.feature.tilersale.model.SaleAvailableDTO;
import com.wangkang.goodwillghservice.feature.tilersale.service.SaleAvailableService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/distributor")
public class DistributorController {


    private final DistributorService distributorService;
    private final SaleAvailableService saleAvailableService;

    public DistributorController(DistributorService distributorService, SaleAvailableService saleAvailableService) {
        this.distributorService = distributorService;
        this.saleAvailableService = saleAvailableService;
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

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_SELF_QUERY')")
    @GetMapping("/sales/available")
    public ResponseEntity<Object> getAvailableSales(Principal principal) {
        UUID uuid = UUID.fromString(principal.getName());
        Collection<SaleAvailableDTO> saleAvailable = saleAvailableService.getSaleAvailable(uuid);
        return ResponseEntity.ok(saleAvailable);
    }
}
