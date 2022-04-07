package zahv.alex.specification;

import lombok.Getter;

@Getter
public enum SearchPattern {
    LEFT("%%%s"),
    RIGHT("%s%%"),
    LEFT_RIGHT("%%%s%%"),
    WITHOUT("%s");

    private String template;

    SearchPattern(String template) {
        this.template = template;
    }
}
