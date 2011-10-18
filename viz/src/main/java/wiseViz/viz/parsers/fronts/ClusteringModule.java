package wiseViz.viz.parsers.fronts;

import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * Parses the trace file entries that relate to the Clustering module.
 */
public class ClusteringModule extends AbstractParser {

    private final String CLUSTER_DECISION = "CLP";

    /**
     * The available colors for clusters.
     */
    private static ArrayList<Integer> availColors;

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public ClusteringModule(final VizPanel vPanel) {
        super(vPanel);
        availColors = new ArrayList<Integer>();
        fillColors();
    }

    public static void addColor(final int color) {
        availColors.add(color);
    }

    /**
     * Add colors for the clusters.
     */
    private void fillColors() {
        availColors.add(Color.RED.getRGB());
        availColors.add(Color.CYAN.getRGB());
        availColors.add(Color.ORANGE.darker().getRGB());
        availColors.add(Color.GREEN.getRGB());
        availColors.add(Color.DARK_GRAY.getRGB());
        availColors.add(Color.MAGENTA.getRGB());
        availColors.add(Color.YELLOW.getRGB());
        availColors.add(Color.PINK.getRGB());
        availColors.add(Color.BLUE.getRGB());
        availColors.add(Color.LIGHT_GRAY.getRGB());
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
        final String thisLine = line.substring(line.indexOf("Text [") + "Text [".length(), line.indexOf("]", line.indexOf("Text [")));

        if (thisLine.indexOf(CLUSTER_DECISION) < 0) {
            return;
        }

        final StringTokenizer stok = new StringTokenizer(thisLine, ";");
        stok.nextToken();
        final String fromNodeId = stok.nextToken();
        final String msgType = stok.nextToken();
        final String cheadNodeId = stok.nextToken();

        final VizNode fromNode = displayNode(fromNodeId);
        final VizNode cheadNode = displayNode(cheadNodeId);

        // Check if node should be ignored
        if ((fromNode == null) || (cheadNode == null)) {
            return;
        }

        // Check if node has announced itself as a clusterhead
        if (msgType.equals("2")) {
            // It is a clusterhead
            // Cluster head election
            if (!fromNode.isClusterHead()) {
                fromNode.setClusterHead(true);

                // Check if we have run out of colors
                if (availColors.size() < 1) {
                    fillColors();
                }

                // Pick a color for the cluster
                fromNode.setColorInt(availColors.remove(0));

                // Hard-coded correlation of Arduino nodes
                VizNode arduinoNode = null;
                switch (fromNode.getId()) {
                    case 378:
                        arduinoNode = super.parent.getNode(0);
                        arduinoNode.setColorInt(fromNode.getColorInt());
                        break;

                    case 3227:
                        arduinoNode = super.parent.getNode(1);
                        arduinoNode.setColorInt(fromNode.getColorInt());
                        break;

                    case 2168:
                        arduinoNode = super.parent.getNode(2);
                        arduinoNode.setColorInt(fromNode.getColorInt());
                        break;
                }
            }
        } else if (msgType.equals("1")) {
            // Simple node -- a follower
            if (fromNode.isClusterHead()) {
                // Maintain color
                availColors.add(fromNode.getColorInt());
            }

            // Make sure node is not a head
            fromNode.setClusterHead(false);

            // Assign same color with head
            fromNode.setColorInt(cheadNode.getColorInt());

            // Hard-coded correlation of Arduino nodes
            VizNode arduinoNode = null;
            switch (fromNode.getId()) {
                case 378:
                    arduinoNode = super.parent.getNode(0);
                    arduinoNode.setColorInt(fromNode.getColorInt());
                    break;

                case 3227:
                    arduinoNode = super.parent.getNode(1);
                    arduinoNode.setColorInt(fromNode.getColorInt());
                    break;

                case 2168:
                    arduinoNode = super.parent.getNode(2);
                    arduinoNode.setColorInt(fromNode.getColorInt());
                    break;
            }
        }
    }


}
