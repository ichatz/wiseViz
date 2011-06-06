package eu.fronts.unified.viz.parsers;

import eu.fronts.unified.viz.base.VizLink;
import eu.fronts.unified.viz.base.VizNode;
import eu.fronts.unified.viz.base.VizPanel;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the E2E module.
 */
public class E2ECache extends AbstractParser {

    private final String E2E_CACHE = "E2EP";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public E2ECache(final VizPanel vPanel) {
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

        if (thisLine.indexOf(E2E_CACHE) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String size = stok.nextToken();

        // Enable/Disable Message
        final String nodeIdMain = extractNodeUrn(line);
        final VizNode thisNode = displayNode(nodeIdMain);

        // Check if node should be ignored
        if (thisNode == null) {
            return;
        }

        thisNode.setBufferSize(Integer.parseInt(size));
    }


}