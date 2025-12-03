package com.wangkang.wangkangdataetlservice.dao.dataetlservice.audit.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class SpecificationUtil {
    private SpecificationUtil() {
    }

    public static void addLikePredicate(CriteriaBuilder cb,
                                        Root<?> root,
                                        List<Predicate> predicates,
                                        String fieldName,
                                        String value) {
        if (StringUtils.isNotBlank(value)) {
            String normalized = value.trim().toLowerCase().replaceAll("\\s+", "");
            predicates.add(cb.like(
                    cb.lower(cb.function("replace", String.class, root.get(fieldName),
                            cb.literal(" "), cb.literal(""))),
                    "%" + normalized + "%"
            ));
        }
    }

    public static <T> void addInPredicate(Root<?> root,
                                          List<Predicate> predicates,
                                          String fieldName,
                                          Collection<T> values) {
        if (!CollectionUtils.isEmpty(values)) {
            predicates.add(root.get(fieldName).in(values));
        }
    }
}
