package wiseViz.plots.parsers;

import org.jfree.chart.ChartPanel;

import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractParser implements Observer {


    public abstract ChartPanel getChart();
}
