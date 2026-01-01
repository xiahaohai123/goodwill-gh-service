package com.wangkang.goodwillghservice.feature.user.tiler;


import com.wangkang.goodwillghservice.feature.tilersale.model.TilerSalesRecordDTO;
import com.wangkang.goodwillghservice.feature.tilersale.service.TilerSalesRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/tiler")
public class TilerController {

    private static final Log log = LogFactory.getLog(TilerController.class);
    private final TilerSalesRecordService tilerSalesRecordService;

    public TilerController(TilerSalesRecordService tilerSalesRecordService) {
        this.tilerSalesRecordService = tilerSalesRecordService;
    }

    @PreAuthorize("hasAuthority('TILER_SELF_QUERY')")
    @GetMapping("/points")
    public ResponseEntity<Object> getPoints(Principal principal) {
        UUID uuid = UUID.fromString(principal.getName());
        int totalPoints = tilerSalesRecordService.getTotalPoints(uuid);
        return ResponseEntity.ok().body(totalPoints);
    }

    @PreAuthorize("hasAuthority('TILER_SELF_QUERY')")
    @GetMapping("/sales")
    public ResponseEntity<Object> getSalesRecords(Principal principal,
                                                  Pageable pageable,
                                                  PagedResourcesAssembler<TilerSalesRecordDTO> assembler) {
        UUID uuid = UUID.fromString(principal.getName());
        Page<TilerSalesRecordDTO> recordPage = tilerSalesRecordService.getRecordPage(uuid, pageable);
        return ResponseEntity.ok().body(assembler.toModel(recordPage));
    }

    @PreAuthorize("hasAnyAuthority('TILER_SELF_QUERY')")
    @GetMapping("/uuid")
    public ResponseEntity<Object> getUUID(Principal principal) {
        log.info("get uuid");
        return ResponseEntity.ok(principal.getName());
    }
}
