package wizeViz.viz.parsers;

import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Aggregation module.
 */
public class SemanticGroupsParser extends AbstractParser {

    private final String SENSOR_VAL = "CLL";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public SemanticGroupsParser(final VizPanel vPanel) {
        super(vPanel);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param obj the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     *            method.
     */
    public void update(final Observable obj, final Object arg) {
        final String line = (String) arg;
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        if (thisLine.indexOf(SENSOR_VAL) < 0) {
            return;
        }

        final String fromNodeId = extractNodeUrn(line);

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");

        stok.nextToken();
        stok.nextToken();

        final String value = stok.nextToken();
        final String gparent = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        System.out.println("" + fromNodeId + " has sema " + value);

        // Check if node should be ignored
        if (fromNode == null) {
            return;
        }

        fromNode.addSemanticGroup(value, gparent);
    }

}

