package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Visualizes the final stages of GKE.
 */
public class GKEClustering extends AbstractParser {

    private final String MSG = "GKE;";
    private final String MSG_LEADER = "GKE_LEADER;";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public GKEClustering(final VizPanel vPanel) {
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

        if (thisLine.indexOf(MSG) < 0 && thisLine.indexOf(MSG_LEADER) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);

        // Check if node should be ignored
        if (fromNode == null) {
            return;
        }

        fromNode.setColorInt(Color.GREEN.getRGB());
    }
}
