package com.wangkang.goodwillghservice.dao.goodwillghservice.product.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.product.model.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TileRepository extends JpaRepository<Tile, UUID>,
        JpaSpecificationExecutor<Tile> {

    List<Tile> findAllByCodeIn(Collection<String> codes);
}
