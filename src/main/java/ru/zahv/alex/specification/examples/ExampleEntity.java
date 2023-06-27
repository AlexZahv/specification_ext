package ru.zahv.alex.specification.examples;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "example")
@Getter
@Setter
public class ExampleEntity {
    @Id
    private Long id;
}
