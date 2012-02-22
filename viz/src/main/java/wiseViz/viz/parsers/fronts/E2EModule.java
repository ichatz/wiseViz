package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the E2E module.
 */
public class E2EModule extends AbstractParser {

    private final String E2E_SEND = "E2E";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public E2EModule(final VizPanel vPanel) {
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

        if (thisLine.indexOf(E2E_SEND) < 0) {
            return;
        }

        if (thisLine.toLowerCase().indexOf("e2ec") >= 0) {
            return;
        }

        if (thisLine.toLowerCase().indexOf("e2ep") >= 0) {
            return;
        }

        if (thisLine.toLowerCase().indexOf("cluster_radio_receive") >= 0) {
            return;
        }


        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        final String msgType = stok.nextToken();
        final String toNodeId = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);

        // Check if node should be ignored
        if ((fromNode == null) || (toNode == null)) {
            return;
        }

        final VizLink linkFwd = fromNode.getLink(toNode.getId());
        final VizLink linkRev = toNode.getLink(fromNode.getId());

        if (linkFwd != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkFwd, Color.RED.getRGB(), 20, fromNode, toNode);

        } else if (linkRev != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkRev, Color.RED.getRGB(), 20, fromNode, toNode);
        }
    }

}
