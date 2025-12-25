package com.wangkang.goodwillghservice.feature.gwtoolstation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goodwill/toolStation")
public class GwToolStationController {

    private final TileService tileService;

    public GwToolStationController(TileService tileService) {
        this.tileService = tileService;
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PutMapping("/tile/sync")
    public ResponseEntity<Object> syncTileData() {
        tileService.updateAllTile();
        return ResponseEntity.noContent().build();
    }
}
