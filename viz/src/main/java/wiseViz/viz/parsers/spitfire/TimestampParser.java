package wiseViz.viz.parsers.spitfire;

import wiseViz.viz.base.VizPanel;
import wiseViz.viz.message.SpitfireMessage;
import wiseViz.viz.parsers.AbstractParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

/**
 * Parses the timestamps of the traces.
 */

public class TimestampParser
        extends AbstractParser {

    private Date lastTime;
    private final String TIME_FORMAT_OUT = "HH:mm:ss";
    private final DateFormat dataVisualizer;


    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public TimestampParser(final VizPanel vPanel) {
        super(vPanel);
        lastTime = null;
        dataVisualizer = new SimpleDateFormat(TIME_FORMAT_OUT);

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
        if (!(arg instanceof String)) {
            return;
        }

        SpitfireMessage message = new SpitfireMessage((String) arg);
        if (message.isValid()) {

            try {
                if (lastTime == null) {
                    lastTime = message.getTimestamp();
                }
                if (message.getTimestamp().before(lastTime))
                    return;
                lastTime = message.getTimestamp();
                // Format time tag & display
                parent.setLastDateTag(dataVisualizer.format(lastTime));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

        }
    }
}


