package wizeViz.viz.parsers;

import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.util.Observable;


/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/25/11
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterRadioModule extends AbstractParser {
    private final String NEWROUTE = "NewRoute";
    private final String REMOVEROUTE = "RemoveRoute";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public ClusterRadioModule(final VizPanel vPanel) {
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
        final String line = (String) arg;


        // HighwayModule Module Message
        final String nodeIdMain = extractNodeUrn(line);
        final VizNode mainNode = displayNode(nodeIdMain);

        // Check if node should be ignored
        if (mainNode == null) {
            return;
        }

        //executed when a new CRadio link is found
        if (line.indexOf(NEWROUTE) > 0) {
            final int strSourceNodeIdStart = line.indexOf("Node") + 4;
            final int strSourceNodeIdStop = line.indexOf("::", strSourceNodeIdStart);
            final String sourceNodeId = line.substring(strSourceNodeIdStart, strSourceNodeIdStop);
            final VizNode srcNode = displayNode(sourceNodeId);

            // Check if node should be ignored
            if (srcNode == null) {
                return;
            }

            final int strTargetNodeIdStart = line.indexOf("through") + 7;
            final int strTargetNodeIdStop = line.indexOf("::", strTargetNodeIdStart);
            final String targetNodeId = line.substring(strTargetNodeIdStart, strTargetNodeIdStop);
            final VizNode tgtNode = displayNode(targetNodeId);

            // Check if node should be ignored
            if (tgtNode == null) {
                return;
            }

            // Check if nodes are the same
            if (tgtNode.getId() == srcNode.getId()) {
                return;
            }

            //System.out.println("Add link "+srcNode.getHexId() + " - " + tgtNode.getHexId());
            // show link
            displayLink(srcNode, tgtNode, VizLink.LINK_CRADIO);

        }
        //executed when a CRadio link is removed
        else if (line.indexOf(REMOVEROUTE) > 0) {

            final int strSourceNodeIdStart = line.indexOf("Node") + 4;
            final int strSourceNodeIdStop = line.indexOf("::", strSourceNodeIdStart);
            final String sourceNodeId = line.substring(strSourceNodeIdStart, strSourceNodeIdStop);
            final VizNode srcNode = displayNode(sourceNodeId);

            // Check if node should be ignored
            if (srcNode == null) {
                return;
            }

            final int strTargetNodeIdStart = line.indexOf("through") + 7;
            final int strTargetNodeIdStop = line.indexOf("::", strTargetNodeIdStart);
            final String targetNodeId = line.substring(strTargetNodeIdStart, strTargetNodeIdStop);
            final VizNode tgtNode = displayNode(targetNodeId);

            // Check if node should be ignored
            if (tgtNode == null) {
                return;
            }

            // Check if nodes are the same
            if (tgtNode.getId() == srcNode.getId()) {
                return;
            }

            //System.out.println("Remove link "+srcNode.getHexId() + " - " + tgtNode.getHexId());
            // show link
            removeLink(srcNode, tgtNode);
        }

    }
}
