package wizeViz.viz.parsers;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Clustering module.
 */
public class ClusterRadioMessages extends AbstractParser {

    private final String CLUSTER_RADIO_SEND = "CLRS";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public ClusterRadioMessages(final VizPanel vPanel) {
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

        if (thisLine.indexOf(CLUSTER_RADIO_SEND) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        final String msgType = stok.nextToken();
        final String toNodeId = stok.nextToken();
        final String payload = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);

        if ((fromNode != null) && (toNode != null)) {
            final VizLink linkFwd = fromNode.getLink(toNode.getId());
            final VizLink linkRev = toNode.getLink(fromNode.getId());

            int color = 0, width = 0;
            if (payload.equals("0x70")) {
                // control - RRQ
                color = Color.MAGENTA.getRGB();
                width = 2;

            } else if (payload.equals("0x71")) {
                // control - RPL
                color = Color.MAGENTA.getRGB();
                width = 2;

            } else if (payload.equals("0x6f")) {
                // data
                color = Color.RED.getRGB();
                width = 20;
            }

            if (linkFwd != null) {
                // enforce link  bidirectionallity
                linkFwd.setType(VizLink.LINK_BI);
                linkFwd.setEnabled(true);

                fromNode.ucastEvent();
                fromNode.sendPacket(linkFwd, color, width, fromNode, toNode);

            } else if (linkRev != null) {
                // enforce link  bidirectionallity
                linkRev.setType(VizLink.LINK_BI);
                linkRev.setEnabled(true);

                fromNode.ucastEvent();
                fromNode.sendPacket(linkRev, color, width, fromNode, toNode);

            } else {
                displayLink(fromNode, toNode, VizLink.LINK_BI);
                fromNode.ucastEvent();
                fromNode.sendPacket(linkFwd, color, width, fromNode, toNode);
            }
        }
    }


}

