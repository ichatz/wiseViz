package wizeViz.viz.parsers.tinyRPL;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;
import wizeViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;

/**
 * Inspects the trace file for DAO packets.
 */
public class DAOPacketParser
        extends AbstractParser {

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public DAOPacketParser(final VizPanel vPanel) {
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
        if (line.length() < 130) {
            // this is not a DAO packet
            return;
        }

        // Extract message type
        final String strMessageType = line.substring(137, 139);
        if (!strMessageType.equals("02")) {
            // this is not a DAO packet
            return;
        }

        // Extract source id
        final String strSource = "0x" + line.substring(39, 43);
        final VizNode thisNode = displayNode(strSource);

        // Broadcast to all neighbors
        for (VizLink vizLink : thisNode.getLinks()) {
            thisNode.ucastEvent();
            if (vizLink.getTarget().getId() == thisNode.getId()) {
                thisNode.sendPacket(vizLink, Color.GREEN.getRGB(), 4, thisNode, vizLink.getTarget());
            } else {
                thisNode.sendPacket(vizLink, Color.GREEN.getRGB(), 4, thisNode, vizLink.getSource());
            }
        }
    }

}