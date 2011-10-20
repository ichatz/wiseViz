package wiseViz.viz.parsers.tinyRPL;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.Color;
import java.lang.Object;
import java.lang.String;
import java.util.Observable;

/**
 * Inspects the trace file for DIS packets.
 */
public class DISPacketParser
        extends AbstractParser {

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public DISPacketParser(final VizPanel vPanel) {
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
            // this is not a DIS packet
            return;
        }

        // locate first space and use it as an offset
        final int startOfTag = line.indexOf(' ') - 2;

        // Extract message type
        final String strMessageType = line.substring(128 + startOfTag, 130 + startOfTag);
        if (!strMessageType.equals("00")) {
            // this is not a DIS packet
            return;
        }

        // Extract source id
        final String strSource = "0x" + line.substring(38 + startOfTag, 41 + startOfTag).replace("0","") + "00";
        final VizNode thisNode = displayNode(strSource);

        // Broadcast to all neighbors
        for (VizLink vizLink : thisNode.getLinks()) {
            thisNode.ucastEvent();
            if (vizLink.getTarget().getId() == thisNode.getId()) {
                thisNode.sendPacket(vizLink, Color.ORANGE.getRGB(), 4, thisNode, vizLink.getTarget());
            } else {
                thisNode.sendPacket(vizLink, Color.ORANGE.getRGB(), 4, thisNode, vizLink.getSource());
            }
        }
    }

}