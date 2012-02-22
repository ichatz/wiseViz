package wiseViz.viz.parsers.tinyRPL;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.lang.Object;
import java.util.Observable;

/**
 * Initializes the nodes of the vizualization.
 */
public class InitVizParser
        extends AbstractParser {

    private boolean completed;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public InitVizParser(final VizPanel vPanel) {
        super(vPanel);
        completed = false;
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
        // make sure it is executed only once
        if (completed) {
            return;
        }

        completed = true;

        // initialize nodes
        final VizNode thisNode1 = displayNode("0x100");
        final VizNode thisNode2 = displayNode("0x200");
        final VizNode thisNode3 = displayNode("0x300");
        final VizNode thisNode4 = displayNode("0x400");
        final VizNode thisNode5 = displayNode("0x500");
        final VizNode thisNode6 = displayNode("0x700");

        // initialize links
        displayLink(thisNode1, thisNode2, VizLink.LINK_BI);
        displayLink(thisNode1, thisNode3, VizLink.LINK_BI);
        displayLink(thisNode2, thisNode4, VizLink.LINK_BI);
        displayLink(thisNode2, thisNode5, VizLink.LINK_BI);
        displayLink(thisNode3, thisNode4, VizLink.LINK_BI);
        displayLink(thisNode3, thisNode5, VizLink.LINK_BI);
        displayLink(thisNode4, thisNode6, VizLink.LINK_BI);
        displayLink(thisNode5, thisNode6, VizLink.LINK_BI);

    }
}
