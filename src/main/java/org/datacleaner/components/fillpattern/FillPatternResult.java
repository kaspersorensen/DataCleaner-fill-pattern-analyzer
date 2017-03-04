package org.datacleaner.components.fillpattern;

import java.util.Collections;
import java.util.List;

import org.datacleaner.api.AnalyzerResult;
import org.datacleaner.api.InputColumn;
import org.datacleaner.storage.RowAnnotationFactory;

public class FillPatternResult implements AnalyzerResult {

    private static final long serialVersionUID = 1L;

    private final List<FillPatternGroup> _fillPatternGroups;
    private final List<InputColumn<?>> _inspectedColumns;
    private final RowAnnotationFactory _rowAnnotationFactory;

    public FillPatternResult(RowAnnotationFactory rowAnnotationFactory, List<InputColumn<?>> inspectedColumns,
            List<FillPatternGroup> fillPatterns) {
        _rowAnnotationFactory = rowAnnotationFactory;
        _inspectedColumns = inspectedColumns;
        _fillPatternGroups = fillPatterns;
    }

    public List<FillPatternGroup> getFillPatternGroups() {
        return Collections.unmodifiableList(_fillPatternGroups);
    }

    public boolean isGrouped() {
        if (_fillPatternGroups.size() == 1 && _fillPatternGroups.get(0).getGroupName() == null) {
            return false;
        }
        return true;
    }

    public RowAnnotationFactory getRowAnnotationFactory() {
        return _rowAnnotationFactory;
    }

    public List<InputColumn<?>> getInspectedColumns() {
        return Collections.unmodifiableList(_inspectedColumns);
    }
}
