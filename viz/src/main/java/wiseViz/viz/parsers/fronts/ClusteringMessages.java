package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.Color;
import java.lang.Object;
import java.lang.String;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Clustering module.
 */
public class ClusteringMessages extends AbstractParser {

    private final String CLUSTER_SEND = "CLS";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public ClusteringMessages(final VizPanel vPanel) {
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

        if (thisLine.indexOf(CLUSTER_SEND) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        final String msgType = stok.nextToken();
        final String toNodeId = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);

        if (toNode == null) {
            // Broadcast to all neighbors
            for (VizLink vizLink : fromNode.getLinks()) {
                fromNode.ucastEvent();
                if (vizLink.getTarget().getId() == fromNode.getId()) {
                    fromNode.sendPacket(vizLink, Color.GREEN.getRGB(), 4, fromNode, vizLink.getTarget());
                } else {
                    fromNode.sendPacket(vizLink, Color.GREEN.getRGB(), 4, fromNode, vizLink.getSource());
                }
            }

        } else {
            final VizLink linkFwd = fromNode.getLink(toNode.getId());
            final VizLink linkRev = toNode.getLink(fromNode.getId());

            if (linkFwd != null) {
                fromNode.ucastEvent();
                fromNode.sendPacket(linkFwd, Color.GREEN.getRGB(), 4, fromNode, toNode);

            } else if (linkRev != null) {
                fromNode.ucastEvent();
                fromNode.sendPacket(linkRev, Color.GREEN.getRGB(), 4, fromNode, toNode);
            }
        }
    }

}
