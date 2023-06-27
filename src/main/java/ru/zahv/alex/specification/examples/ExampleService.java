package ru.zahv.alex.specification.examples;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zahv.alex.specification.CommonFilter;
import ru.zahv.alex.specification.ComparisonEnum;
import ru.zahv.alex.specification.Condition;
import ru.zahv.alex.specification.FilterBuilder;

@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository repository;

    public void exampleFilter(FilterDto filterDto) {
        CommonFilter<ExampleEntity> commonFilter = FilterBuilder.getFilter(filterDto);
        commonFilter.addCondition(new Condition(ComparisonEnum.GT, "id", 222));
        repository.findAll(commonFilter, Sort.by(Sort.Direction.ASC, "id"));
    }
}
