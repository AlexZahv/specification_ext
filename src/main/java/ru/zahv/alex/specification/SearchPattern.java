package ru.zahv.alex.specification;

import lombok.Getter;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
@Getter
public enum SearchPattern {
    LEFT("%%%s"),
    RIGHT("%s%%"),
    LEFT_RIGHT("%%%s%%"),
    WITHOUT("%s");

    private final String template;

    SearchPattern(String template) {
        this.template = template;
    }
}
