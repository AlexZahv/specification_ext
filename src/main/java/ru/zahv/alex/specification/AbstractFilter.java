package ru.zahv.alex.specification;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
@Getter
@Setter
@SuppressWarnings({"all"})
public abstract class AbstractFilter<T> implements Specification<T> {
    protected List<Condition> conditions = new ArrayList<>();

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void addCondition(ComparisonEnum comparisonEnum, String field, Comparable value) {
        if (value instanceof String) {
            if (StringUtils.isNoneBlank((String) value)) {
                conditions.add(new Condition(comparisonEnum, field, value));
            }
        } else if (value != null) {
            conditions.add(new Condition(comparisonEnum, field, value));
        }
    }

    public void addCondition(ComparisonEnum comparisonEnum, String field, Comparable... values) {
        conditions.add(new Condition(comparisonEnum, field, values));
    }

    public void addCondition(ComparisonEnum comparisonEnum, String field, SearchPattern pattern, Comparable... values) {
        conditions.add(new Condition(comparisonEnum, field, pattern, values));
    }

    public void addCondition(ComparisonEnum comparisonEnum, String field, SearchPattern pattern, Comparable value) {
        conditions.add(new Condition(comparisonEnum, field, pattern, value));
    }

    public void addINCondition(String field, Comparable[] value) {
        conditions.add(new Condition(field, value));
    }

    protected Predicate getCommonPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = buildCommonPredicates(root, criteriaQuery, criteriaBuilder);
        if (predicates.isEmpty()) {
            return null;
        } else {
            return predicates.size() > 1
                    ? criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
                    : predicates.get(0);
        }
    }

    protected List<Predicate> buildCommonPredicates(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return conditions.stream()
                .map(condition -> buildPredicate(condition, root, criteriaBuilder))
                .collect(toList());
    }

    private List<Predicate> buildPredicates(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return conditions.stream()
                .map(condition -> buildPredicate(condition, root, criteriaBuilder))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private Predicate buildPredicate(Condition condition, Root root, CriteriaBuilder builder) {

        switch (condition.comparisonEnum) {
            case EQ:
                return builder.equal(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case LIKE:
                return builder.like(builder.lower(getFieldExpression(condition, root, JoinType.INNER)), getStringPattern(condition));
            case IS_TRUE:
                return builder.isTrue(getFieldExpression(condition, root, JoinType.INNER));
            case GT:
                return builder.greaterThan(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case GTE:
                return builder.greaterThanOrEqualTo(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case LT_NOW:
                return builder.lessThanOrEqualTo(getFieldExpression(condition, root, JoinType.INNER), LocalDateTime.now());
            case LT:
                return builder.lessThan(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case LTE:
                return builder.lessThanOrEqualTo(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case NOT_LIKE:
                return builder.notLike(builder.lower(getFieldExpression(condition, root, JoinType.INNER)), getStringPattern(condition));
            case NE:
                return builder.notEqual(getFieldExpression(condition, root, JoinType.INNER), condition.value);
            case NE_LEFT:
                return builder.notEqual(getFieldExpression(condition, root, JoinType.LEFT), condition.value);
            case IS_NULL:
                return getFieldExpression(condition, root, JoinType.LEFT).isNull();
            case IS_EMPTY:
                return builder.isEmpty(getFieldExpression(condition, root, JoinType.LEFT));
            case IS_NOT_NULL:
                return getFieldExpression(condition, root, JoinType.LEFT).isNotNull();
            case IN:
                return getFieldExpression(condition, root, JoinType.INNER).in(condition.values);
            case NOT_IN:
                return getFieldExpression(condition, root, JoinType.INNER).in(condition.values).not();
            case OR_CONDITIONS:
                return builder.or(buildForInnerConditions(condition, root, builder));
            default:
                return builder.equal(getFieldExpression(condition, root, JoinType.INNER), condition.value);
        }
    }

    private Predicate[] buildForInnerConditions(Condition parentCondition, Root root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Condition condition : parentCondition.orConditions) {
            Predicate predicate = buildPredicate(condition, root, builder);
            predicates.add(predicate);
        }

        return predicates.toArray(new Predicate[]{});
    }


    private String getStringPattern(Condition condition) {
        return ofNullable(condition.searchPattern)
                .map(p -> String.format(p.getTemplate(), condition.value).toLowerCase())
                .orElse((String) condition.value);
    }

    private Path getFieldExpression(Condition condition, Root root, JoinType joinType) {
        String[] split = StringUtils.split(condition.field, '.');
        if (split.length > 1) {
            Join join = null;
            for (int i = 0; i < split.length - 1; i++) {
                if (join == null) {
                    join = root.join(split[i], joinType);
                } else {
                    join = join.join(split[i], joinType);
                }
            }
            return join.get(split[split.length - 1]);
        }
        return root.get(condition.field);
    }
}
