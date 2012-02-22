package wizeViz.viz.parsers;

import wizeViz.viz.base.VizPanel;
import wizeViz.viz.message.SpitfireMessage;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/22/12
 * Time: 10:00 AM
 */
public class SpitfireTestObserver extends AbstractParser {

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public SpitfireTestObserver(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable observable, Object o) {
        if (!(o instanceof String)) {
            return;
        }

        SpitfireMessage message = new SpitfireMessage((String) o);

//        System.out.println(message.getApplication());
//        System.out.println(message.getDstIP());
    }
}
