package wiseViz.viz.parsers.tinyRPL;

import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

/**
 * Parses the timestamps of the traces.
 */
public class TimestampParser
        extends AbstractParser {

    private final String TIME_FORMAT_OUT = "yyyy-MM-dd  HH:mm:ss";

    private Date lastSeen;

    private final DateFormat dataVizualizer;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public TimestampParser(final VizPanel vPanel) {
        super(vPanel);
        dataVizualizer = new SimpleDateFormat(TIME_FORMAT_OUT);
        lastSeen = new Date(0);
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

        // locate first and second space
        final int startOfTag = line.indexOf(' ') + 1;
        final int endOfTag = line.indexOf(' ', startOfTag + 1);

        if (startOfTag < 1 || endOfTag < 1) {
            return;
        }

        // Extract timestamp
        final String strTimeTag = line.substring(startOfTag, endOfTag);

        // Try to convert timestamp
        try {
            final Double timeLong = Double.parseDouble(strTimeTag);
            final Date timeTag = new Date((long) timeLong.doubleValue() * 1000);
            if (timeTag.getTime() > lastSeen.getTime()) {
                // keep last known time tag
                lastSeen = timeTag;

                // Format time tag & display
                parent.setLastDateTag(dataVizualizer.format(lastSeen));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
