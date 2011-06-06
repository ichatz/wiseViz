package eu.fronts.unified.viz.tasks;

import eu.fronts.unified.viz.base.VizPanel;

import java.util.TimerTask;

/**
 * Redraw the node events.
 */
public class PulseNodeEvents extends TimerTask {

    /**
     * VizPanel to handle.
     */
    protected final VizPanel vpanel;

    /**
     * Default constructor.
     *
     * @param thisPanel the VizPanel to handle.
     */
    public PulseNodeEvents(final VizPanel thisPanel) {
        vpanel = thisPanel;
    }

    /**
     * Shake the words.
     */
    public void run() {
        try {
            vpanel.tickNodes(12, 0.02f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
