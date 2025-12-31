package com.wangkang.goodwillghservice.feature.tilersale.service;

import com.wangkang.goodwillghservice.feature.tilersale.model.TilerSalesRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TilerSalesRecordService {

    int getTotalPoints(UUID tilerUserId);

    Page<TilerSalesRecordDTO> getRecordPage(UUID tilerUserId, Pageable pageable);
}
