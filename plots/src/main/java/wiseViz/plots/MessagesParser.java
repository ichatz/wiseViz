package wiseViz.plots;

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

import java.awt.*;
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
public class MessagesParser extends AbstractParser {
    private static Logger log = Logger.getLogger(PlotsMain.class);


    private List<String> MessagesPrefixes = new ArrayList<String>();
    private int[] MessagesCounters;

    TimeSeriesCollection dataset = new TimeSeriesCollection();

    private TimeSeries[] EventSeries;
    private int width;
    private int height;
    private int windowSize;

    public MessagesParser(Dimension dim, int windowSize) {
        MessagesPrefixes.add("CLS;");
        MessagesPrefixes.add("CLRS;");
        MessagesPrefixes.add("E2E;");
        MessagesPrefixes.add("AGS;");


        MessagesCounters = new int[MessagesPrefixes.size()];
        EventSeries = new TimeSeries[MessagesPrefixes.size()];
        for (int i = 0; i < MessagesPrefixes.size(); i++) {
            MessagesCounters[i] = 0;
            EventSeries[i] = new TimeSeries(MessagesPrefixes.get(i), Millisecond.class);
            dataset.addSeries(EventSeries[i]);
        }

        width = (int) (dim.width * 0.45);
        height = (int) (dim.height * 0.35);
        this.windowSize = windowSize;

    }

    public void update(Observable o, Object arg) {
        final String line = (String) arg;
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        for (int i = 0; i < MessagesPrefixes.size(); i++) {
            final String eventprefix = MessagesPrefixes.get(i);
            if (thisLine.contains(eventprefix)) {
                MessagesCounters[i]++;
                final Millisecond now = new Millisecond(new Date());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                EventSeries[i].add(now, MessagesCounters[i]);
                //log.info("Got an event!" + eventprefix + " Total of " + MessagesCounters[i]);
            }
        }


    }


    ChartPanel getChart() {
        ChartPanel cp = new ChartPanel(createChart(dataset, "Messages", "Time", "# of Messages"));
        cp.setPreferredSize(new Dimension(width, height));
        return cp;
    }


    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title, String xlabel, String ylabel) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title,
                xlabel,
                ylabel,
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
