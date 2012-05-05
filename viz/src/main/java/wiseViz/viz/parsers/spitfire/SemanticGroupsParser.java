package wiseViz.viz.parsers.spitfire;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.message.Message;
import wiseViz.viz.message.TRMessage;
import wiseViz.viz.parsers.AbstractParser;

import java.util.Observable;

/**
 * Parses the trace file entries that relate to the Aggregation module.
 */
public class SemanticGroupsParser extends AbstractParser {

//    private final String SENSOR_VAL = "CLL";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public SemanticGroupsParser(final VizPanel vPanel) {
        super(vPanel);
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
        if (!(arg instanceof String)) {
            return;
        }

        Message message = new TRMessage((String) arg);
        if (!message.isValid()) return;

        if (message.getPayload().contains("id::")) {

            if (message.getPayload().contains("SELEFT")) {

            } else if (message.getPayload().contains("SE")) {

                final int indexStart = message.getPayload().indexOf("SE");
                final String textLine = message.getPayload().substring(indexStart);
                String nodeID = textLine.split(" ")[1];
                final String seline = textLine.substring(message.getPayload().indexOf("ex:") + 3);
                final String semanticEntity = seline.split(" ")[0];
                final String entityID = semanticEntity.substring(getNumericIndex(semanticEntity));
                final String entityName = semanticEntity.substring(0, getNumericIndex(semanticEntity)).replaceAll("ex:", "");
                final VizNode fromNode = displayNode(nodeID);
                fromNode.addSensorValue(entityName, entityID);
            }

            if (message.getPayload().contains("ARR")) {
                String textline = message.getPayload().substring(message.getPayload().indexOf("ARR"));
                String[] parts = textline.split(" ");
                String source = parts[2];
                String target = parts[3];
                final VizNode fromNode = displayNode(source);
                final VizNode toNode = displayNode(target);
                displayLink(fromNode, toNode, VizLink.LINK_CRADIO);
            }
        }
//        if (thisLine.indexOf(SENSOR_VAL) < 0) {
//            return;
//        }
//
//        final String fromNodeId = extractNodeUrn(line);
//
//        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
//
//        stok.nextToken();
//        stok.nextToken();
//
//        final String value = stok.nextToken();
//        final String gparent = stok.nextToken();
//
//        final VizNode fromNode = displayNode(fromNodeId);
//        System.out.println("" + fromNodeId + " has sema " + value);
//
//        // Check if node should be ignored
//        if (fromNode == null) {
//            return;
//        }
//
//        fromNode.addSemanticGroup(value, gparent);
    }


    public int getNumericIndex(final String str) {

        if (str == null) {
            return -1;
        }

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                System.out.println("char is : " + str + " @ " + i);
                return i;
            }
        }

        return str.length();
    }

}

