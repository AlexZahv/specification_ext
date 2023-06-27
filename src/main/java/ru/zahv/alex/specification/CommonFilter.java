package ru.zahv.alex.specification;


import lombok.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
public class CommonFilter<T> extends AbstractFilter<T> {

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, CriteriaQuery<?> criteriaQuery,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        criteriaQuery.distinct(true);
        return getCommonPredicate(root, criteriaQuery, criteriaBuilder);
    }
}
