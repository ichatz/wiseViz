package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Aggregation module.
 */
public class AggregationMessages extends AbstractParser {

    private final String AG_RCV = "AGGR";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public AggregationMessages(final VizPanel vPanel) {
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

        if (thisLine.indexOf(AG_RCV) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String toNodeId = stok.nextToken();
        stok.nextToken(); // ignore type
        final String fromNodeId = stok.nextToken();
        stok.nextToken(); // ignore type

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);

        // Check if node should be ignored
        if ((fromNode == null) && (toNode == null)) {
            return;
        }

        final VizLink linkFwd = fromNode.getLink(toNode.getId());
        final VizLink linkRev = toNode.getLink(fromNode.getId());

        if (linkFwd != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkFwd, Color.BLUE.getRGB(), 16, toNode, fromNode);

        } else if (linkRev != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkRev, Color.BLUE.getRGB(), 16, toNode, fromNode);
        }
    }

}
