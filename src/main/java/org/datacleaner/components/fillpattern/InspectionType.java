package org.datacleaner.components.fillpattern;

import org.apache.metamodel.util.HasName;
import org.datacleaner.util.LabelUtils;

public enum InspectionType implements HasName {

    NULL_BLANK_OR_FILLED("Null, blank or filled"),

    NULL_OR_FILLED("Null or filled"),

    DISTINCT_VALUES("Distinct values");

    private String _name;

    private InspectionType(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Object inspect(Object value) {
        if (value == null) {
            return LabelUtils.NULL_LABEL;
        }
        final boolean isBlank = value instanceof String && ((String) value).trim().isEmpty();
        switch (this) {
        case NULL_OR_FILLED:
            return FillPatternAnalyzer.FILLED_LABEL;
        case NULL_BLANK_OR_FILLED:
            if (isBlank) {
                return LabelUtils.BLANK_LABEL;
            }
            return FillPatternAnalyzer.FILLED_LABEL;
        case DISTINCT_VALUES:
            if (isBlank) {
                return LabelUtils.BLANK_LABEL;
            }
            return value;
        }
        throw new UnsupportedOperationException("Unsupported inspection type: " + this);
    }
}
