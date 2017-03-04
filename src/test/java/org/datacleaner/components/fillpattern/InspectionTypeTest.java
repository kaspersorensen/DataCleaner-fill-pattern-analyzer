package org.datacleaner.components.fillpattern;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InspectionTypeTest {

    @Test
    public void testNullOrFilled() {
        final InspectionType it = InspectionType.NULL_OR_FILLED;
        assertEquals("<null>", it.inspect(null));
        assertEquals("<filled>", it.inspect(1));
        assertEquals("<filled>", it.inspect("hello"));
        assertEquals("<filled>", it.inspect("  "));
        assertEquals("<filled>", it.inspect(""));
    }

    @Test
    public void testNullBlankOrFilled() {
        final InspectionType it = InspectionType.NULL_BLANK_OR_FILLED;
        assertEquals("<null>", it.inspect(null));
        assertEquals("<filled>", it.inspect(1));
        assertEquals("<filled>", it.inspect("hello"));
        assertEquals("<blank>", it.inspect("  "));
        assertEquals("<blank>", it.inspect(""));
    }

    @Test
    public void testNullOrNot() {
        final InspectionType it = InspectionType.DISTINCT_VALUES;
        assertEquals("<null>", it.inspect(null));
        assertEquals(1, it.inspect(1));
        assertEquals("hello", it.inspect("hello"));
        assertEquals("<blank>", it.inspect("  "));
        assertEquals("<blank>", it.inspect(""));
    }
}
