package com.wangkang.goodwillghservice.feature.geo;


import com.wangkang.goodwillghservice.dao.goodwillghservice.geo.model.LocationNode;
import com.wangkang.goodwillghservice.dao.goodwillghservice.geo.repository.LocationNodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    private final GhanaLocationSyncService ghanaLocationSyncService;
    private final LocationNodeRepository locationNodeRepository;

    public GeoController(GhanaLocationSyncService ghanaLocationSyncService,
                         LocationNodeRepository locationNodeRepository) {
        this.ghanaLocationSyncService = ghanaLocationSyncService;
        this.locationNodeRepository = locationNodeRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<Object> updateGeoData() {
        ghanaLocationSyncService.syncLocations(false);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取加纳地理位置级联树
     * 过滤掉逻辑删除的数据，并按 Region -> District -> Locality 组装
     */
    @GetMapping("/ghana/hierarchy")
    public List<LocationHierarchyDTO> getGhanaHierarchy() {
        // 1. 获取所有未删除的节点
        List<LocationNode> allNodes = locationNodeRepository.findAll()
                .stream()
                .filter(node -> !node.getDeleted())
                .toList();

        // 2. 将所有节点转为 DTO 映射表
        Map<Long, LocationHierarchyDTO> dtoMap = allNodes.stream()
                .collect(Collectors.toMap(
                        LocationNode::getId,
                        node -> new LocationHierarchyDTO(node.getId(), node.getName(), node.getLevel())
                ));

        List<LocationHierarchyDTO> rootNodes = new ArrayList<>();

        // 3. 构建树形结构
        for (LocationNode node : allNodes) {
            LocationHierarchyDTO currentDto = dtoMap.get(node.getId());
            if (node.getParentId() == null || node.getLevel() == 1) {
                // 一级节点 (Region)
                rootNodes.add(currentDto);
            } else {
                // 子节点，挂载到父节点下
                LocationHierarchyDTO parentDto = dtoMap.get(node.getParentId());
                if (parentDto != null) {
                    parentDto.getChildren().add(currentDto);
                }
            }
        }

        return rootNodes;
    }
}
