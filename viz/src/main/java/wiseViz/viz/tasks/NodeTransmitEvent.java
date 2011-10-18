package wiseViz.viz.tasks;

import wiseViz.viz.base.VizNode;

import java.util.TimerTask;

/**
 * Used to indicate periodic transmission of beacons.
 */
public class NodeTransmitEvent extends TimerTask {

    /**
     * VizPanel to handle.
     */
    protected final VizNode vnode;

    /**
     * Default constructor.
     *
     * @param thisNode the VizNode to handle.
     */
    public NodeTransmitEvent(final VizNode thisNode) {
        vnode = thisNode;
    }

    /**
     * Shake the words.
     */
    public void run() {
        try {
            synchronized (vnode) {
                vnode.bcastEvent();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

