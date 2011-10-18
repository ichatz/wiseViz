package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.util.Observable;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Vizualization for the Group Key Establishment Module.
 */
public class GKEInstalledKeys extends AbstractParser {

    private final String KEYS = "GKE_KEY";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public GKEInstalledKeys(final VizPanel vPanel) {
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

        if (thisLine.indexOf(KEYS) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        TreeSet<Integer> keys = new TreeSet<Integer>();
        while(stok.hasMoreTokens()) {
            keys.add(Integer.valueOf(stok.nextToken()));
	    stok.nextToken();
        }

        final VizNode fromNode = displayNode(fromNodeId);

        // Check if node should be ignored
        if (fromNode == null) {
            return;
        }

        // Update the node
        fromNode.setKeys(keys);
    }

}
