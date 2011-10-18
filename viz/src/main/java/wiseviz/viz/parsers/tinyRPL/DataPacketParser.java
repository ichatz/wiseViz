package wizeViz.viz.parsers.tinyRPL;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;
import wizeViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;

/**
 * Inspects the trace file for DATA packets.
 */
public class DataPacketParser
        extends AbstractParser {

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public DataPacketParser(final VizPanel vPanel) {
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

        // Check length of packet
        if (line.length() < 210) {
            // this is not a DATA packet
            return;
        }

        // Extract message type
        final String strMessageType = line.substring(58, 60);
        if (!strMessageType.equals("11")) {
            // this is not a DATA packet
            return;
        }

        // Extract source id
        final String strSource = "0x" + line.substring(92, 94) + "00";
        final VizNode fromNode = displayNode(strSource);

        // Extract target id
        final String strTarget = "0x" + line.substring(124, 126) + "00";
        final VizNode toNode = displayNode(strTarget);

        final VizLink linkFwd = fromNode.getLink(toNode.getId());
        final VizLink linkRev = toNode.getLink(fromNode.getId());

        if (linkFwd != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkFwd, Color.BLUE.getRGB(), 16, fromNode, toNode);

        } else if (linkRev != null) {
            fromNode.ucastEvent();
            fromNode.sendPacket(linkRev, Color.BLUE.getRGB(), 16, fromNode, toNode);
        }
    }

}

