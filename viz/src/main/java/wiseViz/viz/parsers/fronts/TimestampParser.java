package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizPanel;
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

    private final String TIME_TAG_START = "Time [";

    private final String TIME_TAG_END = ".";

    private final String TIME_FORMAT_IN = "yyyy-MM-dd'T'HH:mm:ss";

//    private final String TIME_FORMAT_OUT = "yyyy-MM-dd  HH:mm:ss";
    private final String TIME_FORMAT_OUT = "HH:mm:ss";

    private Date lastSeen;

    private final DateFormat dataFormatter, dataVizualizer;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public TimestampParser(final VizPanel vPanel) {
        super(vPanel);
        dataFormatter = new SimpleDateFormat(TIME_FORMAT_IN);
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

        // Check if trace contains Timestamp
        final int startOfTag = line.indexOf(TIME_TAG_START) + TIME_TAG_START.length();
        if (startOfTag > TIME_TAG_START.length()) {

            // Locate end of timestamp
            final int endOfTag = line.indexOf(TIME_TAG_END, startOfTag);

            // Extract timestamp
            final String strTimeTag = line.substring(startOfTag, endOfTag);

            // Try to convert timestamp
            try {
                Date timeTag = dataFormatter.parse(strTimeTag);
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

}


