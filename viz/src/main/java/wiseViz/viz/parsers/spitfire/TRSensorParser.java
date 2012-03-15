package wiseViz.viz.parsers.spitfire;

import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.message.Message;
import wiseViz.viz.message.TRMessage;
import wiseViz.viz.parsers.AbstractParser;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/23/12
 * Time: 2:02 PM
 */
public class TRSensorParser extends AbstractParser {
    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public TRSensorParser(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) {
            return;
        }

        Message message = new TRMessage((String) arg);
        if (!message.isValid()) return;

        if (message.getPayload().contains("id::")) {

            System.out.println(message.getPayload() + "" + message.getPayload().split(" ").length);
            final int idStart = message.getPayload().indexOf("::") + 2;
            final int idStop = message.getPayload().indexOf(" ", idStart);
            final String nodeId = message.getPayload().substring(idStart, idStop);

            final VizNode thisNode = displayNode(nodeId);
            final String[] parts = message.getPayload().split(" ");
            try {
                double value = Double.parseDouble(parts[2]);
                thisNode.addSensorValue(parts[1], String.valueOf(value));
            } catch (NumberFormatException nfe) {

            }


        }
    }
}
