package zahv.alex.specification;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FilterOptions {
    SearchPattern searchPattern() default SearchPattern.LEFT_RIGHT;

    ComparisonEnum operation() default ComparisonEnum.EQ;

    String fieldName() default "";

    boolean convertDateToDateTime() default false;
}
