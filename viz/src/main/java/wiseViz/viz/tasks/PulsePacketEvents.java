package wiseViz.viz.tasks;

import wiseViz.viz.base.VizPanel;

import java.lang.Exception;
import java.util.TimerTask;

/**
 * Redraw the packet events.
 */
public class PulsePacketEvents extends TimerTask {

    /**
     * VizPanel to handle.
     */
    protected final VizPanel vpanel;

    /**
     * Default constructor.
     *
     * @param thisPanel the VizPanel to handle.
     */
    public PulsePacketEvents(final VizPanel thisPanel) {
        vpanel = thisPanel;
    }

    /**
     * Shake the words.
     */
    public void run() {
        try {
            vpanel.tickPackets();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

