package wizeViz.viz.parsers;

import wizeViz.viz.VizProperties;
import wizeViz.viz.base.VizLink;
import wizeViz.viz.base.VizNode;
import wizeViz.viz.base.VizPanel;

import java.awt.*;
import java.util.Observer;
import java.util.StringTokenizer;

/**
 * Abstract parser of Trace file lines.
 */
public abstract class AbstractParser implements Observer {

    private final String URN;
    /**
     * Parent Vizualization Panel.
     */
    protected final VizPanel parent;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public AbstractParser(final VizPanel vPanel) {
        parent = vPanel;
        URN = VizProperties.getInstance().getProperty(VizProperties.TESTBED_URN) + ":";
    }

    /**
     * Adds a new node in the network.
     *
     * @param strNodeId the node ID.
     * @return the VizNode representation.
     */
    protected final VizNode displayNode(final String strNodeId) {
        VizNode node = null;

        if (strNodeId.toLowerCase().contains("0xffff")) {
            // Broadcast destination address
            // ignore
            return null;
        }

        if ((strNodeId.toLowerCase().contains("0x4d78"))
                || (strNodeId.toLowerCase().contains("0x5eef"))
                || (strNodeId.toLowerCase().contains("0x55c7"))) {
            // Broadcast destination address
            // ignore
            return null;
        }

        // Check if this node is already displayed
        final int nodeId = convertNodeId(strNodeId);

        if (parent.containsNode(nodeId)) {
            // already exists
            node = parent.getNode(nodeId);

            parent.setupArduinoLinks(node);

        } else {
            final int totNodes = parent.getNodesSize();
            float posX = 200 + (totNodes % 4) * 300;
            float posY = 100 + (totNodes / 4) * 250;

            // check if the node is supposed to be ignored
            Boolean isIgnored = VizProperties.getInstance().getProperty(VizProperties.NODE_IGNORE + "0x" + Integer.toHexString(nodeId), false);
            if (isIgnored) {
                return node;
            }

            // try to load position of node
            String strPos = VizProperties.getInstance().getProperty(VizProperties.NODE_POSITION + "0x" + Integer.toHexString(nodeId));
            if (strPos != null) {
                StringTokenizer stok = new StringTokenizer(strPos, ",");
                posX = Float.parseFloat(stok.nextToken());
                posY = Float.parseFloat(stok.nextToken());

                posX *= parent.getScaleX();
                posY *= parent.getScaleY();

                posX += parent.getOffsetX();
                posY += parent.getOffsetY();
            }

            int fontColor = Color.WHITE.getRGB();
            if (parent.isShowBGMap()) {
                fontColor = Color.BLACK.getRGB();
            }

            node = parent.displayNode(nodeId,
                    posX, posY,
                    Color.WHITE.getRGB(), // Inner color
                    Color.ORANGE.getRGB(), // Send Msg color
                    fontColor); // Font color
        }

        return node;
    }

    /**
     * Adds a new link in the network.
     *
     * @param srcNode  the source Node.
     * @param tgtNode  the target Node.
     * @param linkType -- the type of the link.
     * @return the VizLink representation.
     */
    public final VizLink displayLink(final VizNode srcNode, final VizNode tgtNode, final int linkType) {
        if ((srcNode == null) || (tgtNode == null)) {
            return null;
        }
        return parent.displayLink(srcNode, tgtNode, linkType);
    }

    /**
     * Adds a new link in the network.
     *
     * @param srcNode the source Node.
     * @param tgtNode the target Node.
     */
    public void removeLink(final VizNode srcNode, final VizNode tgtNode) {
        parent.removeLink(srcNode, tgtNode);
    }

    final String PREFIX = ";";

    final String PREFIX_S = "Node;";

    /**
     * Extract the node ID from the log file.
     *
     * @param line -- the String containing the node ID to extract.
     * @return the nodeID as string.
     */
    protected String extractNodeUrn(final String line) {
        int strNodeIdStart = 0;
        int strNodeIdStop = 0;
        if (line.indexOf("NB") > 0) {
            strNodeIdStart = line.indexOf(PREFIX) + PREFIX.length();
            strNodeIdStop = line.indexOf(";", line.indexOf(PREFIX) + PREFIX.length());
        } else {
            strNodeIdStart = line.indexOf(URN) + URN.length();
            strNodeIdStop = line.indexOf("]", strNodeIdStart);
        }

        return line.substring(strNodeIdStart, strNodeIdStop);
    }

    /**
     * Extract the node ID from the log file.
     *
     * @param line   -- the String containing the node ID to extract.
     * @param prefix -- the String prefix that proceed the node ID.
     * @return the nodeID as string.
     */
    protected String extractNodeTarget(final String line, final String prefix) {
        final int strNodeIdStart = line.indexOf(prefix) + prefix.length();
        final int strNodeIdStop = line.indexOf(";", strNodeIdStart);
        return line.substring(strNodeIdStart, strNodeIdStop);
    }

    /**
     * Extract the node ID from the log file.
     *
     * @param line   -- the String containing the node ID to extract.
     * @param prefix -- the String prefix that preceedes the node ID.
     * @return the nodeID as string.
     */
    protected String extractNodeId(final String line, final String prefix) {
        final int strNodeIdStart = line.indexOf(prefix) + prefix.length();

        // Try to extract nodeID as if it was the last info of the debug line
        String strNodeId = line.substring(strNodeIdStart, line.length() - 1);

        // Check if the nodeID is not the end of the debug message but more info follows
        if (line.indexOf("::", strNodeIdStart) > 0) {
            // ignore trailing info
            final int strNodeIdEnd = line.indexOf("::", strNodeIdStart);
            strNodeId = line.substring(strNodeIdStart, strNodeIdEnd);

        } else if (line.indexOf(" ", strNodeIdStart) > 0) {
            // ignore trailing info
            final int strNodeIdEnd = line.indexOf(" ", strNodeIdStart);
            strNodeId = line.substring(strNodeIdStart, strNodeIdEnd);
        }

        return strNodeId;
    }

    /**
     * Convert the string id into a decimal.
     *
     * @param nodeId the node id (hex or decimal format) as a String.
     * @return the nodeId as decimal integer.
     */
    protected int convertNodeId(final String nodeId) {
        try {
            if (nodeId.indexOf("x") > 0) {
                // in hex
                return Integer.parseInt(nodeId.substring(2), 16);
            } else {
                // in decimal
                return Integer.parseInt(nodeId);
            }
        } catch (Exception ex) {
            return 0;
        }
    }
}
