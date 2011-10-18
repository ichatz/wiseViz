package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the high-level Application.
 */
public class AppRadioMessages extends AbstractParser {

    private final String APP_RADIO_SEND = "FLS";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public AppRadioMessages(final VizPanel vPanel) {
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

        if (thisLine.indexOf(APP_RADIO_SEND) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ":");
        stok.nextToken();
        final String type = stok.nextToken();
        final String param = stok.nextToken();

        final VizNode fromNode = parent.getArduinoNode(0);
        final VizNode toNode = displayNode("0x017a");

        if ((fromNode != null) && (toNode != null)) {
            final VizLink linkFwd = fromNode.getLink(toNode.getId());
            final VizLink linkRev = toNode.getLink(fromNode.getId());

            int color = 0, width = 0;
            // data
            color = Color.RED.getRGB();
            width = 20;

            if (linkFwd != null) {
                fromNode.ucastEvent();
                fromNode.sendPacket(linkFwd, color, width, fromNode, toNode);

            } else if (linkRev != null) {
                fromNode.ucastEvent();
                fromNode.sendPacket(linkRev, color, width, fromNode, toNode);
            }
        }
    }


}
