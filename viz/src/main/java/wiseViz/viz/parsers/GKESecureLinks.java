package wizeViz.viz.parsers;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Visualizing Secure Links.
 */
public class GKESecureLinks extends AbstractParser {

    private final String GKE_SL_SEND = "GKE_SL";
    private static int cnt = 0;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public GKESecureLinks(final VizPanel vPanel) {
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

        if (thisLine.indexOf(GKE_SL_SEND) < 0) {
            return;
        }

	cnt++;
	
        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        final String toNodeId = stok.nextToken();
        final String keyID = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode toNode = displayNode(toNodeId);
        
        if(cnt%2 == 1) {
	       	final VizLink linkNew = displayLink(fromNode, toNode, VizLink.LINK_SL);
	}


/*
        if ((fromNode != null) && (toNode != null)) {
            final VizLink linkFwd = fromNode.getLink(toNode.getId());
            final VizLink linkRev = toNode.getLink(fromNode.getId());

            if (linkFwd != null) {
                // enforce link  bidirectionallity
                linkFwd.setType(VizLink.LINK_SL);
                linkFwd.setEnabled(true);
                linkFwd.setKey(Integer.valueOf(keyID));

            } else if (linkRev != null) {
                // enforce link  bidirectionallity
                linkRev.setType(VizLink.LINK_SL);
                linkRev.setEnabled(true);
                linkRev.setKey(Integer.valueOf(keyID));
	    }
            else {
            	System.out.println("secure link: (" + fromNodeId + ", " + toNodeId + ") with key " + keyID);
            	final VizLink linkNew = displayLink(fromNode, toNode, VizLink.LINK_SL);
                linkNew.setKey(Integer.valueOf(keyID));
            }
        }
*/
    }


}

