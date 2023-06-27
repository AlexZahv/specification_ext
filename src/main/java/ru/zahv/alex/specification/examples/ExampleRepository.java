package ru.zahv.alex.specification.examples;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaSpecificationExecutor<ExampleEntity> {
}
