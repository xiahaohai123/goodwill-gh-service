package com.wangkang.goodwillghservice.feature.user.manager;


import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudCustomerService;
import com.wangkang.goodwillghservice.feature.user.distributor.Distributor4ManagerDTO;
import com.wangkang.goodwillghservice.feature.user.distributor.DistributorExternalInfoDTO;
import com.wangkang.goodwillghservice.feature.user.distributor.DistributorService;
import com.wangkang.goodwillghservice.feature.user.tiler.TilerService;
import com.wangkang.goodwillghservice.share.util.BizAssert;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final DistributorService distributorService;
    private final K3cloudCustomerService k3cloudCustomerService;
    private final TilerService tilerService;

    public ManagerController(DistributorService distributorService, K3cloudCustomerService k3cloudCustomerService,
                             TilerService tilerService) {
        this.distributorService = distributorService;
        this.k3cloudCustomerService = k3cloudCustomerService;
        this.tilerService = tilerService;
    }

    @PreAuthorize("hasAuthority('DISTRIBUTOR_QUERY')")
    @GetMapping("/list/distributor")
    public ResponseEntity<Object> getDistributorList(Pageable pageable,
                                                     PagedResourcesAssembler<Distributor4ManagerDTO> assembler) {
        Page<Distributor4ManagerDTO> page = distributorService.getDistributors(pageable);
        return ResponseEntity.ok(assembler.toModel(page));
    }

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_MODIFY')")
    @PutMapping("/distributor/external")
    public ResponseEntity<Object> updateDistributorExternal() {
        distributorService.updateDistributorExternal();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DISTRIBUTOR_QUERY')")
    @GetMapping("/list/distributor/external")
    public ResponseEntity<Object> getDistributorExternalList(Pageable pageable,
                                                             PagedResourcesAssembler<DistributorExternalInfoDTO> assembler) {
        Page<DistributorExternalInfoDTO> page = distributorService.getDistributorsExternalList(pageable);
        return ResponseEntity.ok(assembler.toModel(page));
    }

    @PreAuthorize("hasAuthority('DISTRIBUTOR_QUERY')")
    @GetMapping("/list/distributor/external/unbound")
    public ResponseEntity<Object> getUnboundDistributorExternalList(Pageable pageable,
                                                                    PagedResourcesAssembler<DistributorExternalInfoDTO> assembler) {
        Page<DistributorExternalInfoDTO> page = distributorService.getUnboundDistributorsExternal(pageable);
        return ResponseEntity.ok(assembler.toModel(page));
    }

    /**
     * 直接从金蝶云取客户数据并返回，调试用接口
     * @return 客户数据
     */
    @PreAuthorize("hasAuthority('DISTRIBUTOR_QUERY') and hasAuthority('DEBUG')")
    @GetMapping("/distributor/external/k3cloud")
    public ResponseEntity<Object> updateDistributorExternalK3cloud() {
        List<Customer> customerList = k3cloudCustomerService.getCustomerList();
        return ResponseEntity.ok(customerList);
    }

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_MODIFY')")
    @PutMapping("/distributor/bind")
    public ResponseEntity<Object> bindDistributor(@Valid @RequestBody BindDistributorDTO bindDistributorDTO) {
        distributorService.bindDistributor2External(bindDistributorDTO.getUserId(), bindDistributorDTO.getExternalId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_MODIFY')")
    @DeleteMapping("/distributor/bind")
    public ResponseEntity<Object> unbindDistributor2External(@RequestBody BindDistributorDTO bindDistributorDTO) {
        UUID userId = bindDistributorDTO.getUserId();
        BizAssert.notNull(userId, "user.null");
        distributorService.unbindDistributor2External(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/distributor/baseline")
    public ResponseEntity<Object> updateSaleAvailableCalBaseLine(@Valid @RequestBody AvailableSalesCalBaselineDTO dto) {
        UUID distributorId = dto.getUserId();
        OffsetDateTime baseline = dto.getBaseline();
        distributorService.updateAvailableSalesCalBaseline4Distributor(distributorId, baseline);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('TILER_QUERY')")
    @GetMapping("/tiler")
    public ResponseEntity<Object> getTilerList(Pageable pageable,
                                               PagedResourcesAssembler<User> assembler) {
        Page<User> page = tilerService.getTilerPage(pageable);
        return ResponseEntity.ok(assembler.toModel(page));
    }
}
