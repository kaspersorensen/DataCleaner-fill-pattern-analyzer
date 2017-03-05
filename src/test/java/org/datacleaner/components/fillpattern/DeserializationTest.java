package org.datacleaner.components.fillpattern;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.util.stream.Collectors;

import org.datacleaner.result.AnalysisResult;
import org.datacleaner.util.ChangeAwareObjectInputStream;
import org.junit.Test;

public class DeserializationTest {

    // tests that an analysis result of this type can be deserialized. The
    // example result was created manually based on Open Street Map (OSM) data.
    @Test
    public void testDeserializeAnalysisResult() throws Exception {
        final AnalysisResult result;
        try (final FileInputStream in = new FileInputStream(new File(
                "example/Fill-pattern-analysis-OSM-example.analysis.result.dat"))) {
            try (final ChangeAwareObjectInputStream changeAwareObjectInputStream = new ChangeAwareObjectInputStream(
                    in)) {
                final Object obj = changeAwareObjectInputStream.readObject();

                result = (AnalysisResult) obj;
            }
        }

        assertEquals(1, result.getResults().size());

        final FillPatternResult fillPatternResult = (FillPatternResult) result.getResults().get(0);

        final String str = fillPatternResult.getFillPatternGroups().stream().map(r -> r.getGroupName() + "=" + r
                .getPatternCount()).collect(Collectors.joining(","));
        assertEquals(
                "<null>=54,US=26,DE=20,GB=20,AT=14,SE=13,CH=12,IT=12,FI=11,ES=10,SK=9,FR=8,NL=7,NO=6,LU=6,CZ=5,BE=5,"
                        + "PL=5,IS=5,EE=4,RU=4,RO=4,DK=3,IE=3,BG=3,SI=2,HU=2,LT=2,LV=2,GR=2,BY=1,HR=1,IM=1,GE=1,RS=1,MT=1,CY=1",
                str);
    }
}
