package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Parses the trace file entries that relate to the high-level Application.
 */
public class NodeDisable extends AbstractParser {

    private final String NODE_DISABLE = "OFF";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public NodeDisable(final VizPanel vPanel) {
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

        if (thisLine.indexOf(NODE_DISABLE) < 0) {
            return;
        }

        // Enable/Disable Message
        final String nodeIdMain = extractNodeUrn(line);
        final VizNode thisNode = displayNode(nodeIdMain);

        if (thisNode != null) {
            // Make sure node is not a head
            thisNode.setClusterHead(false);

            // Assign same color with head
            thisNode.setColorInt(Color.WHITE.getRGB());
            thisNode.setAggregatedValue("");
            thisNode.setSensorValue("");
            thisNode.setEnabled(false);

            // Remove links
            ArrayList<VizLink> toRemove = new ArrayList<VizLink>(thisNode.getLinks());
            for (VizLink vizLink : toRemove) {
                vizLink.setEnabled(false);
                parent.removeLink(vizLink.getSource(), vizLink.getTarget());
                parent.removeLink(vizLink.getTarget(), vizLink.getSource());
            }

            thisNode.clearLinks();
            thisNode.setKeys(null);
        }
    }
}