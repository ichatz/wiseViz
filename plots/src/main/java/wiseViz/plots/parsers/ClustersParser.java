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

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClustersParser extends AbstractParser {
    private static Logger log = Logger.getLogger(PlotsMain.class);

    private TimeSeries clusters_series;
    private TimeSeries cluster_size_series;


    private TimeSeriesCollection dataset = new TimeSeriesCollection();

    private static String ClustersPrefix = "CLP";

    private HashMap<Integer, Integer> HeadNodes = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> SimpleNodes = new HashMap<Integer, Integer>();
    private int width;
    private int height;
    private int windowSize;

    public ClustersParser(Dimension dim, int windowSize) {
        clusters_series = new TimeSeries("Clusters", Millisecond.class);
        cluster_size_series = new TimeSeries("Avg Cluster Size", Millisecond.class);
        dataset.addSeries(clusters_series);
        dataset.addSeries(cluster_size_series);

        width = (int) (dim.width * 0.45);
        height = (int) (dim.height * 0.35);
        this.windowSize = windowSize;
    }

    public void update(Observable o, Object arg) {
        final String line = (String) arg;
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        if (thisLine.contains("CLP")) {
            int clp_start = thisLine.indexOf(ClustersPrefix) + ClustersPrefix.length();
            int clp_end = thisLine.lastIndexOf(";");
            String clpevent = thisLine.substring(clp_start, clp_end);


            String[] a = clpevent.split(";");
            int type = Integer.parseInt(a[2]);
            int nodeid = Integer.parseInt(a[1].substring(2), 16);


            if (type == 2) {
                set_head(nodeid);
                log.info("added head");
            } else if (type != 0) {
                set_simple(nodeid);
                log.info("added simple");
            }

            final Millisecond now = new Millisecond(new Date());


            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            log.info("head:" + heads() + " simple:" + simples());
            if (heads() > 0) {
                clusters_series.add(now, heads());
                cluster_size_series.add(now, total() / heads());
//                log.info("Found clp," + nodeid + "," + type + " Addin:" + heads() + " div: " + total() / heads());
            } else {
                clusters_series.add(now, 0);
                cluster_size_series.add(now, 0);
            }

        }


    }

    private void set_head(int id) {
        if (HeadNodes.containsKey(id)) {
            return;
        } else if (SimpleNodes.containsKey(id)) {
            SimpleNodes.remove(id);
//            log.info("adding now:" + HeadNodes.size());
            HeadNodes.put(id, 1);
//            log.info("finished now:" + HeadNodes.size());
        } else {
            HeadNodes.put(id, 1);
        }
    }

    private void set_simple(int id) {
        if (SimpleNodes.containsKey(id)) {
            return;
        } else if (HeadNodes.containsKey(id)) {
            HeadNodes.remove(id);
            SimpleNodes.put(id, 1);
        } else {
            SimpleNodes.put(id, 1);
        }
    }

    private double heads() {
        return HeadNodes.size();
    }

    private double simples() {
        return SimpleNodes.size();
    }

    private double total() {

        return SimpleNodes.size() + HeadNodes.size();
    }


    ChartPanel getChart() {
        ChartPanel cp = new ChartPanel(createChart(dataset, "Clusters", "Time", "# of Nodes"));
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
