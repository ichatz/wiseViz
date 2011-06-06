package wizeViz.viz.parsers;

import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.awt.*;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Visualizes the final stages of GKE.
 */
public class GKEStatMessage extends AbstractParser {

    private final String MSG = "GKE_STAT;";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public GKEStatMessage(final VizPanel vPanel) {
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
        

        if (thisLine.indexOf(MSG) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
	
	// last element on the ';' separated list is ...
        String tmp;
        do {
        	tmp = stok.nextToken();
        } while(stok.hasMoreElements());

	// "0" if has no key, "1" if has GROUPKEY
	if( tmp.indexOf("1") < 0 ) {
		return;
	}

	final VizNode fromNode = displayNode(tmp);

        // Check if node should be ignored
        if (fromNode == null) {
            return;
        }

        fromNode.setColorInt(Color.GREEN.getRGB());
    }
}
