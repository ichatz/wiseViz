package wizeViz.viz.parsers.fronts;

import wizeViz.viz.base.VizPanel;
import wizeViz.viz.parsers.AbstractParser;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleParser extends AbstractParser {


    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public ExampleParser(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable o, Object arg) {
        // Here you can add the code parse the incoming message and change the visual output
    }
}
