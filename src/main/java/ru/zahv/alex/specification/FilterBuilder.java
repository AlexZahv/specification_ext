package ru.zahv.alex.specification;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
@Slf4j
@SuppressWarnings({"all"})
public class FilterBuilder {
    public static <T, E> CommonFilter<E> getFilter(T t) {
        return ofNullable(t)
                .map(obj -> {
                    CommonFilter<E> filter = new CommonFilter<>();
                    filter.setConditions(getCommonFilterConditions(obj));
                    return filter;
                })
                .orElse(null);
    }

    public static <T> List<Condition> getCommonFilterConditions(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        return Stream.of(fields)
                .map(field -> {
                    try {
                        return getCondition(t, field);
                    } catch (IllegalAccessException e) {
                        log.error("Could not get condition", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T> Condition getCondition(T t, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = field.get(t);
        if (fieldValue == null
                || (fieldValue instanceof String && fieldValue.equals(""))
                || (field.getAnnotation(FilterOptions.class) == null)
        ) {
            return null;
        }

        FilterOptions filterOptions = field.getAnnotation(FilterOptions.class);
        ComparisonEnum comparisonOperation = ofNullable(filterOptions).map(FilterOptions::operation)
                .orElse(ComparisonEnum.EQ);

        if (fieldValue instanceof String
                && ofNullable(filterOptions).isPresent()
                && filterOptions.operation().equals(ComparisonEnum.LIKE)
        ) {
            return new Condition(
                    ofNullable(filterOptions).map(FilterOptions::operation).orElse(ComparisonEnum.LIKE),
                    getFiledName(field, filterOptions),
                    getStringValue((String) fieldValue, filterOptions.searchPattern())
            );

        } else if (fieldValue instanceof Date) {
            return new Condition(comparisonOperation, getFiledName(field, filterOptions), (Date) fieldValue);

        } else if (fieldValue instanceof LocalDate) {
            Boolean convertDateToDateTime
                    = ofNullable(filterOptions).map(FilterOptions::convertDateToDateTime).orElse(false);

            LocalDate dateValue = (LocalDate) fieldValue;

            return convertDateToDateTime
                    ? new Condition(comparisonOperation, getFiledName(field, filterOptions), dateValue.atStartOfDay())
                    : new Condition(comparisonOperation, getFiledName(field, filterOptions), (LocalDate) fieldValue);

        } else if (fieldValue instanceof LocalDateTime) {
            LocalDateTime dateValue = (LocalDateTime) fieldValue;
            return  new Condition(comparisonOperation, getFiledName(field, filterOptions), dateValue);

        } else if (fieldValue instanceof Boolean) {
            return new Condition(comparisonOperation, getFiledName(field, filterOptions), (Boolean) fieldValue);
        } else if (fieldValue instanceof Collection) {
            Comparable[] array = ((Collection<Comparable>) fieldValue).toArray(new Comparable[]{});
            return new Condition(comparisonOperation, getFiledName(field, filterOptions), array);
        }
        return new Condition(comparisonOperation, getFiledName(field, filterOptions), fieldValue.toString());
    }

    private static String getFiledName(Field field, FilterOptions filterOptions) {
        return StringUtils.isEmpty(ofNullable(filterOptions).map(FilterOptions::fieldName).orElse(""))
                ? field.getName()
                : filterOptions.fieldName();
    }

    private static String getStringValue(String value, SearchPattern searchPattern) {
        return ofNullable(searchPattern)
                .map(p -> String.format(p.getTemplate(), value))
                .orElse(String.format(SearchPattern.LEFT_RIGHT.getTemplate(), value));
    }
}
