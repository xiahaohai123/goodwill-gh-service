package com.wangkang.goodwillghservice.feature.user.manager;


import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudCustomerService;
import com.wangkang.goodwillghservice.feature.user.UserDTO;
import com.wangkang.goodwillghservice.feature.user.distributor.DistributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final DistributorService distributorService;
    private final K3cloudCustomerService k3cloudCustomerService;

    public ManagerController(DistributorService distributorService, K3cloudCustomerService k3cloudCustomerService) {
        this.distributorService = distributorService;
        this.k3cloudCustomerService = k3cloudCustomerService;
    }

    @GetMapping("/list/distributor")
    public ResponseEntity<Object> getDistributorList() {
        List<UserDTO> distributors = distributorService.getDistributors();
        return ResponseEntity.ok(distributors);
    }

    @PreAuthorize("hasAnyAuthority('DISTRIBUTOR_MODIFY')")
    @PutMapping("/distributor/external")
    public ResponseEntity<Object> updateDistributorExternal() {
        distributorService.updateDistributorExternal();
        return ResponseEntity.noContent().build();
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
}
