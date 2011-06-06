package wizeViz.viz.parsers;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Neighborhood discovery module.
 */
public class NeighborhoodModule extends AbstractParser {

    private final String ECHO_UNI = "NB";

    private final String ECHO_BIDI = "NBB";

    private final String ECHO_LOST = "NBD";

    private final String ECHO_LOST_BIDI = "NBL";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public NeighborhoodModule(final VizPanel vPanel) {
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

        if (thisLine.indexOf(ECHO_UNI) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        final String msgType = stok.nextToken();
        final String toNodeId = stok.nextToken();
        final String fromNodeId = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);

        // Check if node should be ignored
        if ((fromNode == null) || (toNode == null)) {
            return;
        }

        // Set Link or Remove Link depending on debug message
        if (msgType.equals(ECHO_UNI)) {
            displayLink(fromNode, toNode, VizLink.LINK_UNI);

        } else if (msgType.equals(ECHO_BIDI)) {
            displayLink(fromNode, toNode, VizLink.LINK_BI);

        } else if (msgType.equals(ECHO_LOST)) {
            removeLink(fromNode, toNode);

        } else if (msgType.equals(ECHO_LOST_BIDI)) {
            displayLink(fromNode, toNode, VizLink.LINK_UNI);
        }
    }

}
