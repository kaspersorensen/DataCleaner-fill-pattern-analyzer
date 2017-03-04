package org.datacleaner.components.fillpattern.swing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.metamodel.util.Ref;
import org.datacleaner.api.InputColumn;
import org.datacleaner.api.Provided;
import org.datacleaner.api.RendererBean;
import org.datacleaner.bootstrap.DCWindowContext;
import org.datacleaner.bootstrap.WindowContext;
import org.datacleaner.components.fillpattern.FillPatternGroup;
import org.datacleaner.components.fillpattern.FillPatternResult;
import org.datacleaner.components.fillpattern.FillPatternsBuilder;
import org.datacleaner.configuration.DataCleanerConfigurationImpl;
import org.datacleaner.data.MockInputColumn;
import org.datacleaner.data.MockInputRow;
import org.datacleaner.panels.DCPanel;
import org.datacleaner.result.renderer.AbstractRenderer;
import org.datacleaner.result.renderer.RendererFactory;
import org.datacleaner.result.renderer.SwingRenderingFormat;
import org.datacleaner.storage.InMemoryRowAnnotationFactory2;
import org.datacleaner.storage.RowAnnotationFactory;
import org.datacleaner.user.UserPreferencesImpl;
import org.datacleaner.util.LabelUtils;
import org.datacleaner.util.LookAndFeelManager;
import org.datacleaner.widgets.DCCollapsiblePanel;
import org.jdesktop.swingx.VerticalLayout;

@RendererBean(SwingRenderingFormat.class)
public class FillPatternResultSwingRenderer extends AbstractRenderer<FillPatternResult, JComponent> {

    @Inject
    @Provided
    WindowContext windowContext;

    @Inject
    @Provided
    RendererFactory rendererFactory;

    @Override
    public JComponent render(FillPatternResult fillPatternResult) {
        final DCPanel panel = new DCPanel();
        panel.setLayout(new VerticalLayout(0));

        if (fillPatternResult.isGrouped()) {

            final List<FillPatternGroup> groups = fillPatternResult.getFillPatternGroups();
            for (FillPatternGroup group : groups) {
                if (panel.getComponentCount() != 0) {
                    panel.add(Box.createVerticalStrut(10));
                }
                final int recordCount = group.getTotalObservationCount();
                final int patternCount = group.getPatternCount();

                final String text = group.getGroupName() + " (" + (patternCount == 1 ? "1 pattern"
                        : patternCount + " patterns") + ", " + (recordCount == 1 ? "1 record"
                                : recordCount + " records") + ")";
                final Ref<? extends JComponent> componentRef = () -> new FillPatternGroupPanel(windowContext,
                        rendererFactory, fillPatternResult, group);
                final DCCollapsiblePanel collapsiblePanel = new DCCollapsiblePanel(text, text, patternCount < 2,
                        componentRef);
                panel.add(collapsiblePanel.toPanel());
            }
        } else {
            panel.add(new FillPatternGroupPanel(windowContext, rendererFactory, fillPatternResult, fillPatternResult
                    .getFillPatternGroups().get(0)));
        }

        return panel;
    }

    public static void main(String[] args) {
        LookAndFeelManager.get().init();

        final DataCleanerConfigurationImpl configuration = new DataCleanerConfigurationImpl();
        final FillPatternResultSwingRenderer renderer = new FillPatternResultSwingRenderer();
        renderer.rendererFactory = new RendererFactory(configuration);
        renderer.windowContext = new DCWindowContext(configuration, new UserPreferencesImpl(null), null);

        final InputColumn<?> col1 = new MockInputColumn<>("foo");
        final InputColumn<?> col2 = new MockInputColumn<>("bar");
        final InputColumn<?> col3 = new MockInputColumn<>("baz");

        final RowAnnotationFactory rowAnnotationFactory = new InMemoryRowAnnotationFactory2();
        final List<InputColumn<?>> inspectedColumns = new ArrayList<>();
        inspectedColumns.add(col1);
        inspectedColumns.add(col2);
        inspectedColumns.add(col3);

        final List<FillPatternGroup> fillPatterns = new ArrayList<>();

        final FillPatternsBuilder fillPatternsBuilder = new FillPatternsBuilder(rowAnnotationFactory);
        fillPatternsBuilder.addObservation(new MockInputRow().put(col1, "hello"), Arrays.asList("<filled>",
                LabelUtils.NULL_LABEL, LabelUtils.NULL_LABEL));
        fillPatternsBuilder.addObservation(new MockInputRow().put(col1, "").put(col2, "world"), Arrays.asList(
                LabelUtils.BLANK_LABEL, "<filled>", LabelUtils.NULL_LABEL));

        fillPatterns.add(fillPatternsBuilder.build("group1"));

        final FillPatternResult fillPatternResult = new FillPatternResult(rowAnnotationFactory, inspectedColumns,
                fillPatterns);
        final JComponent renderedResult = renderer.render(fillPatternResult);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.getContentPane().add(renderedResult);
        frame.setVisible(true);
    }
}
