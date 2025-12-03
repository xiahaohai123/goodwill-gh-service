package com.wangkang.wangkangdataetlservice.metabase;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.InventoryPO;
import com.wangkang.wangkangdataetlservice.k3cloud.service.K3cloudMaterialService;
import com.wangkang.wangkangdataetlservice.k3cloud.service.K3cloudOrderService;
import com.wangkang.wangkangdataetlservice.metabase.sale.SaleOrder;
import com.wangkang.wangkangdataetlservice.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/BI")
public class BIController {

    private final K3cloudOrderService k3cloudOrderService;
    private final K3cloudMaterialService k3cloudMaterialService;
    private final BIBusinessService biBusinessService;

    public BIController(K3cloudOrderService k3cloudOrderService,
                        K3cloudMaterialService k3cloudMaterialService,
                        BIBusinessService biBusinessService) {
        this.k3cloudOrderService = k3cloudOrderService;
        this.k3cloudMaterialService = k3cloudMaterialService;
        this.biBusinessService = biBusinessService;
    }

    @PostMapping("/order/date/view")
    public ResponseEntity<Object> viewOrder4BIDateBefore(@RequestBody Map<String, String> dateMap) {
        String beforeDate = dateMap.get("before");
        String afterDate = dateMap.get("after");
        Collection<SaleOrder> orderBIListByTime = k3cloudOrderService.getOrderBIListByCreateDate(afterDate, beforeDate);
        return ResponseEntity.ok(orderBIListByTime);
    }

    @PostMapping("/order/date")
    public ResponseEntity<Object> updateOrder4BIDate(@RequestBody Map<String, String> dateMap) {
        String beginDate = dateMap.get("after");
        String endDate = dateMap.get("before");
        biBusinessService.updateOrder2BI(beginDate, endDate);
        return ResponseBuilder.ok("Succeed to update order");
    }

    @GetMapping("/inventory")
    public ResponseEntity<Object> getInventory4BI() {
        List<InventoryPO> material4BI = k3cloudMaterialService.getMaterial4BI();
        return ResponseBuilder.ok(material4BI);
    }

    @PutMapping("/inventory")
    public ResponseEntity<Object> updateMaterial() {
        int affectedRow = biBusinessService.updateInventory();
        return ResponseBuilder.ok("Succeed to update " + affectedRow + " rows for inventory");
    }

    @PostMapping("/inventory/final/snapshot")
    public ResponseEntity<Object> snapshotFinalGoods() {
        int affectedRow = biBusinessService.snapshotFinalGoods();
        return ResponseBuilder.ok("Succeed to snapshot final goods for " + affectedRow + " rows");
    }
}
