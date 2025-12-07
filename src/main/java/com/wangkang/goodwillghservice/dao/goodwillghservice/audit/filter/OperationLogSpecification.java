package com.wangkang.goodwillghservice.dao.goodwillghservice.audit.filter;

import com.wangkang.goodwillghservice.feature.audit.entity.OperationLogFilterDTO;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.OperationLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OperationLogSpecification {

    private OperationLogSpecification() {
    }

    public static Specification<OperationLog> filterBy(OperationLogFilterDTO dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getOperationName() != null && !dto.getOperationName().isBlank()) {
                predicates.add(cb.like(root.get("operationName"), "%" + dto.getOperationName().trim() + "%"));
            }

            if (dto.getOperationType() != null && !dto.getOperationType().isBlank()) {
                predicates.add(cb.equal(root.get("operationType"), dto.getOperationType().trim()));
            }

            if (dto.getExecutor() != null && !dto.getExecutor().isBlank()) {
                predicates.add(cb.like(root.get("executor"), "%" + dto.getExecutor().trim() + "%"));
            }

            if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
                predicates.add(cb.equal(root.get("status"), dto.getStatus().trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
