package zahv.alex.specification;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Condition {
    public enum Type {
        NUMERIC,
        STRING,
        DATE,
        BOOL,
        UUID,
        LIST,
        RAW
    }

    public Type type;

    public ComparisonEnum comparisonEnum;
    public SearchPattern searchPattern;

    public Comparable value;
    public Object[] values;

    public String field;

    public Condition[] orConditions;

    public Condition(ComparisonEnum comparisonEnum, String field, Comparable value) {
        this.comparisonEnum = comparisonEnum;
        this.value = value;
        this.field = field;
    }

    public Condition(Type type, String field, Comparable[] values) {
        this.type = type;
        this.comparisonEnum = ComparisonEnum.IN;
        this.values = values;
        this.field = field;
    }

    public Condition(ComparisonEnum comparisonEnum, Condition[] conditions) {
        this.comparisonEnum = comparisonEnum;
        this.orConditions = conditions;
    }

    public Condition(ComparisonEnum comparisonEnum, String field, Comparable... values) {
        this.comparisonEnum = comparisonEnum;
        this.values = values;
        this.field = field;
    }

    public Condition(ComparisonEnum comparisonEnum, String field, SearchPattern pattern, Comparable... values) {
        this.comparisonEnum = comparisonEnum;
        this.values = values;
        this.field = field;
        this.searchPattern = pattern;
    }

    public Condition(ComparisonEnum comparisonEnum, String field, SearchPattern pattern, Comparable value) {
        this.comparisonEnum = comparisonEnum;
        this.value = value;
        this.field = field;
        this.searchPattern = pattern;
    }

    public Condition(String field, Object[] values) {
        this.comparisonEnum = ComparisonEnum.IN;
        this.values = values;
        this.field = field;
    }


    public Condition(Type type, ComparisonEnum comparisonEnum, String field, Comparable[] values) {
        this.type = type;
        this.comparisonEnum = comparisonEnum;
        this.values = values;
        this.field = field;
    }

    public Condition(Type type, ComparisonEnum comparisonEnum, String field, Comparable value) {
        this.type = type;
        this.comparisonEnum = comparisonEnum;
        this.value = value;
        this.field = field;
    }

    public Condition(ComparisonEnum comparisonEnum, String field, Comparable value, SearchPattern searchPattern) {
        this(comparisonEnum, field, value);
        this.searchPattern = searchPattern;
    }

    public static class Builder {
        private ComparisonEnum comparisonEnum;
        private Comparable value;
        private String field;

        public Builder setComparisonEnum(ComparisonEnum comparisonEnum) {
            this.comparisonEnum = comparisonEnum;
            return this;
        }

        public Builder setValue(Comparable value) {
            this.value = value;
            return this;
        }

        public Builder setField(String field) {
            this.field = field;
            return this;
        }

        public Condition build() {
            return new Condition(comparisonEnum, field, value);
        }
    }
}
