package org.datacleaner.components.fillpattern;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FillPatternGroup implements Iterable<FillPattern>, Comparable<FillPatternGroup>, Serializable {

    private static final long serialVersionUID = 1L;

    private final String _groupName;
    private final List<FillPattern> _patterns;

    public FillPatternGroup(String groupName, List<FillPattern> patterns) {
        _groupName = groupName;
        _patterns = patterns;
    }

    public String getGroupName() {
        return _groupName;
    }

    @Override
    public Iterator<FillPattern> iterator() {
        return _patterns.iterator();
    }

    public List<FillPattern> asList() {
        return Collections.unmodifiableList(_patterns);
    }

    public int getPatternCount() {
        return _patterns.size();
    }

    public int getTotalObservationCount() {
        return _patterns.stream().collect(Collectors.summingInt(p -> p.getObservationCount()));
    }

    @Override
    public int compareTo(FillPatternGroup other) {
        int diff = other.getPatternCount() - getPatternCount();
        if (diff == 0) {
            diff = other.getTotalObservationCount() - getTotalObservationCount();
            if (diff == 0) {
                // at this point it does not matter, we just don't want to
                // return them as equal
                diff = other.hashCode() - hashCode();
            }
        }
        return diff;
    }
}
