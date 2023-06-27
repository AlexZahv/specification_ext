package ru.zahv.alex.specification.examples;

import lombok.Getter;
import lombok.Setter;
import ru.zahv.alex.specification.ComparisonEnum;
import ru.zahv.alex.specification.FilterOptions;

@Getter
@Setter
public class FilterDto {
    @FilterOptions(fieldName = "id", operation = ComparisonEnum.EQ)
    private Long id;
    @FilterOptions(fieldName = "id", operation = ComparisonEnum.LT)
    private Integer lessThen;
}
