package eu.fronts.unified.viz.parsers;

import eu.fronts.unified.viz.base.VizLink;
import eu.fronts.unified.viz.base.VizNode;
import eu.fronts.unified.viz.base.VizPanel;

import java.util.Observable;

/**
 * Parses the trace file entries that relate to the Highway module.
 */
public class HighwayModule extends AbstractParser {

    private final String HWY_PORT = "HWY_PORTS";

    private final String HWY_DELETE = "HWY_DEL";

    private final String HWY_EDGE = "HWY_EDGE;";

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public HighwayModule(final VizPanel vPanel) {
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

        if (line.indexOf(HWY_PORT) > 0) {
            // HighwayModule Discovery Module -- Marking a node as a port
            //final String nodeIdSender = extractNodeTarget(line, HWY_PORT);
            //final VizNode tgtNode = displayNode(nodeIdSender);

            // show link
            //displayLink(mainNode, tgtNode, VizLink.LINK_UNI);

        } else if (line.indexOf(HWY_EDGE) > 0) {
            // HighwayModule Discovery Module -- Marking an edge as highway edge
            final int strSourceNodeIdStart = line.indexOf(HWY_EDGE) + HWY_EDGE.length() + 1;
            final int strSourceNodeIdStop = line.indexOf(";", strSourceNodeIdStart);
            final String sourceNodeId = line.substring(strSourceNodeIdStart, strSourceNodeIdStop);
            final VizNode srcNode = displayNode(sourceNodeId);

            // Check if node should be ignored
            if (srcNode == null) {
                return;
            }

            final int strTargetNodeIdStop = line.indexOf("]", strSourceNodeIdStop + 2);
            final String targetNodeId = line.substring(strSourceNodeIdStop + 2, strTargetNodeIdStop);
            final VizNode tgtNode = displayNode(targetNodeId);

            // Check if node should be ignored
            if (tgtNode == null) {
                return;
            }

            // Check if nodes are the same
            if (tgtNode.getId() == srcNode.getId()) {
                return;
            }

            // show link
            displayLink(srcNode, tgtNode, VizLink.LINK_SL);

        } else if (line.indexOf(HWY_DELETE) > 0) {
            // HighwayModule Discovery Module -- Removing mark from the edge
        }
    }

}
