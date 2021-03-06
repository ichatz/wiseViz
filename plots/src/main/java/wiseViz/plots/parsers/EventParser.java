package wiseViz.plots.parsers;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import wiseViz.plots.PlotsMain;
import wiseViz.plots.parsers.AbstractParser;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventParser extends AbstractParser {
    private static final Logger log = Logger.getLogger(PlotsMain.class);


    private final List<String> EventPrefixes = new ArrayList<String>();
    private final int[] EventCounters;

    private final TimeSeriesCollection dataset = new TimeSeriesCollection();

    private final TimeSeries[] EventSeries;
    private final int width;
    private final int height;
    private final int windowSize;

    public EventParser(Dimension dim, int windowSize) {
        EventPrefixes.add("NB");
        EventPrefixes.add("CLP");

        EventCounters = new int[EventPrefixes.size()];
        EventSeries = new TimeSeries[EventPrefixes.size()];
        for (int i = 0; i < EventPrefixes.size(); i++) {
            EventCounters[i] = 0;
            EventSeries[i] = new TimeSeries(EventPrefixes.get(i), Millisecond.class);
            dataset.addSeries(EventSeries[i]);
        }


        width = (int) (dim.width * 0.45);
        height = (int) (dim.height * 0.35);
        this.windowSize = windowSize;

    }

    public void update(Observable o, Object arg) {
        final String line = (String) arg;
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        for (int i = 0; i < EventPrefixes.size(); i++) {
            final String eventprefix = EventPrefixes.get(i);
            if (thisLine.contains(eventprefix)) {
                EventCounters[i]++;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                log.info("Got an event!" + eventprefix + " Total of " + EventCounters[i]);
            }
        }


        for (int i = 0; i < EventPrefixes.size(); i++) {
            final Millisecond now = new Millisecond(new Date());
            EventSeries[i].add(now, EventCounters[i]);
        }


    }


    public ChartPanel getChart() {
        ChartPanel cp = new ChartPanel(createChart());
        cp.setPreferredSize(new Dimension(width, height));
        return cp;
    }


    /**
     * Creates a sample chart.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart() {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Events",
                "Time",
                "# of Events",
                dataset,
                true,
                true,
                false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(windowSize * 1000.0);  // 60 seconds
        return result;
    }
}
