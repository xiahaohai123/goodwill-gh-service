package com.wangkang.goodwillghservice.dao.goodwillghservice.geo.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.geo.model.LocationNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LocationNodeRepository extends JpaRepository<LocationNode, UUID>,
        JpaSpecificationExecutor<LocationNode> {

    Optional<LocationNode> findByPCodeAndSource(String pCode, String source);

    /**
     * 智能软删除：将不在本次 P-Code 名单中的 HDX 数据标记为已删除，
     * 但如果该节点下存在手动录入的数据（source = 'MANUAL'），则跳过（保护关联性）。
     */
    @Modifying
    @Query("UPDATE LocationNode n SET n.isDeleted = true " +
            "WHERE n.source = 'HDX' AND n.pCode NOT IN :activePcodes " +
            "AND NOT EXISTS (SELECT 1 FROM LocationNode child WHERE child.parentId = n.id AND child.source = 'MANUAL')")
    void markObsoleteHdxNodes(@Param("activePcodes") Set<String> activePcodes);

    // 新增：获取 HDX 数据中最新的更新时间
    @Query("SELECT MAX(n.updatedAt) FROM LocationNode n WHERE n.source = 'HDX'")
    Optional<OffsetDateTime> findMaxUpdatedAtBySource();
}
