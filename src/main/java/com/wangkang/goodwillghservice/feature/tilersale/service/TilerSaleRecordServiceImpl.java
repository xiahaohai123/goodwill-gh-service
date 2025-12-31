package com.wangkang.goodwillghservice.feature.tilersale.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecord;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository.TilerSalesRecordRepository;
import com.wangkang.goodwillghservice.feature.tilersale.model.TilerSalesRecordDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TilerSaleRecordServiceImpl implements TilerSalesRecordService {
    private final TilerSalesRecordRepository tilerSalesRecordRepository;

    public TilerSaleRecordServiceImpl(TilerSalesRecordRepository tilerSalesRecordRepository) {
        this.tilerSalesRecordRepository = tilerSalesRecordRepository;
    }

    @Override
    public int getTotalPoints(UUID tilerUserId) {
        List<TilerSalesRecord> allRecord = tilerSalesRecordRepository.findAllByTilerId(tilerUserId);
        // TODO 目前的积分计算策略先当作一箱一分，后续打磨期允许动态计算以及分期应用
        return allRecord.stream().mapToInt(TilerSalesRecord::getQuantity).sum();
    }

    @Override
    public Page<TilerSalesRecordDTO> getRecordPage(UUID tilerUserId, Pageable pageable) {
        Page<TilerSalesRecord> recordPage = tilerSalesRecordRepository.findAllByTilerId(tilerUserId, pageable);
        return recordPage.map(po -> {
            TilerSalesRecordDTO tilerSalesRecordDTO = new TilerSalesRecordDTO();
            BeanUtils.copyProperties(po, tilerSalesRecordDTO);
            return tilerSalesRecordDTO;
        });
    }
}
