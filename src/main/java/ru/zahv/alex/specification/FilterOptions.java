package ru.zahv.alex.specification;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FilterOptions {
    SearchPattern searchPattern() default SearchPattern.LEFT_RIGHT;

    ComparisonEnum operation() default ComparisonEnum.EQ;

    String fieldName() default StringUtils.EMPTY;

    boolean convertDateToDateTime() default false;
}
