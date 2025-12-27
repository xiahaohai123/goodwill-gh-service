package com.wangkang.goodwillghservice.feature.k3cloud;


import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/k3cloud/order")
public class K3cloudOrderController {

    private final K3cloudOrderService k3cloudOrderService;

    public K3cloudOrderController(K3cloudOrderService k3cloudOrderService) {
        this.k3cloudOrderService = k3cloudOrderService;
    }

    /**
     * 同步修改的订单
     * @param overlap 同步最后修改时间为 T - overlap (s) 的订单
     * @return 同步行数
     */
    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PutMapping("/{overlap}")
    public ResponseEntity<Object> syncModifiedOrder(@PathVariable long overlap) {
        int i = k3cloudOrderService.syncModifiedOrder(overlap);
        return ResponseEntity.ok(i);
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @DeleteMapping
    public ResponseEntity<Object> syncDeletedOrder() {
        return ResponseEntity.noContent().build();
    }
}
