package org.datacleaner.components.fillpattern;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.datacleaner.storage.RowAnnotation;

public class FillPattern implements Comparable<FillPattern>, Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Object> _fillOutcomes;
    private final RowAnnotation _rowAnnotation;

    public FillPattern(List<Object> fillOutcomes, RowAnnotation rowAnnotation) {
        _fillOutcomes = fillOutcomes;
        _rowAnnotation = rowAnnotation;
    }

    public int getObservationCount() {
        return _rowAnnotation.getRowCount();
    }

    public RowAnnotation getRowAnnotation() {
        return _rowAnnotation;
    }

    public List<Object> getFillOutcomes() {
        return Collections.unmodifiableList(_fillOutcomes);
    }

    @Override
    public int compareTo(FillPattern other) {
        int diff = other.getObservationCount() - getObservationCount();
        if (diff == 0) {
            diff = other._fillOutcomes.hashCode() - _fillOutcomes.hashCode();
            if (diff == 0) {
                // at this point it does not matter, we just don't want to
                // return them as equal
                diff = other.hashCode() - hashCode();
            }
        }
        return diff;
    }

}
