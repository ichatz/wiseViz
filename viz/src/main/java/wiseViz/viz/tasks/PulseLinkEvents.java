package wiseViz.viz.tasks;

import wiseViz.viz.base.VizPanel;

import java.util.TimerTask;

/**
 * Redraw the links events.
 */
public class PulseLinkEvents extends TimerTask {

    /**
     * VizPanel to handle.
     */
    protected final VizPanel vpanel;

    /**
     * Default constructor.
     *
     * @param thisPanel the VizPanel to handle.
     */
    public PulseLinkEvents(final VizPanel thisPanel) {
        vpanel = thisPanel;
    }

    /**
     * Shake the words.
     */
    public void run() {
        try {
            vpanel.tickLinks(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
