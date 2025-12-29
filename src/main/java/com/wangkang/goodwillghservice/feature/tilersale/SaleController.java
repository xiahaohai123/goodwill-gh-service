package com.wangkang.goodwillghservice.feature.tilersale;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private final SaleAvailableService saleAvailableService;

    public SaleController(SaleAvailableService saleAvailableService) {
        this.saleAvailableService = saleAvailableService;
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/all")
    public ResponseEntity<Object> generateSnapshot4AllDistributor() {
        saleAvailableService.takeFullSnapshot4AllDistributor();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PostMapping("/snapshot/{distributorId}")
    public ResponseEntity<Object> generateSnapshot4SpecificDistributor(@PathVariable String distributorId){
        return ResponseEntity.ok().build();
    }
}
