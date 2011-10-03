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
import wiseViz.plots.PlotsMain;

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
public class ApplicationParser extends AbstractParser {
    private static Logger log = Logger.getLogger(PlotsMain.class);


    private final List<String> ApplicationPrefixes = new ArrayList<String>();
    private final int[] ApplicationCounters;

    private final TimeSeriesCollection dataset = new TimeSeriesCollection();

    private final TimeSeries[] EventSeries;
    private final int width;
    private final int height;
    private final int windowSize;

    public ApplicationParser(Dimension dim, int windowSize) {
        ApplicationPrefixes.add("FLS:lamp"); //send to lamp
        ApplicationPrefixes.add("FLS:fan"); //send to fan
        ApplicationPrefixes.add("FLR:lamp"); //lamp received
        ApplicationPrefixes.add("FLR:fan"); //fan received


        ApplicationCounters = new int[ApplicationPrefixes.size()];
        EventSeries = new TimeSeries[ApplicationPrefixes.size()];
        for (int i = 0; i < ApplicationPrefixes.size(); i++) {
            ApplicationCounters[i] = 0;
            EventSeries[i] = new TimeSeries(ApplicationPrefixes.get(i), Millisecond.class);
            dataset.addSeries(EventSeries[i]);
        }

        width = (int) (dim.width * 0.45);
        height = (int) (dim.height * 0.35);
        this.windowSize = windowSize;

    }

    public void update(Observable o, Object arg) {
        final String line = (String) arg;
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        for (int i = 0; i < ApplicationPrefixes.size(); i++) {
            final String eventprefix = ApplicationPrefixes.get(i);
            if (thisLine.contains(eventprefix)) {
                ApplicationCounters[i]++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        for (int i = 0; i < ApplicationPrefixes.size(); i++) {
            final Millisecond now = new Millisecond(new Date());
            EventSeries[i].add(now, ApplicationCounters[i]);
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
                "Application",
                "Time",
                "# of Application",
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
