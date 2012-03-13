package wiseViz.viz.base;

import processing.core.PApplet;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.SortedSet;

/**
 * Represents a network node.
 */
public class VizNode {

    /**
     * The ratio for the size of the round corners.
     */
    private final static float WIDTH_RATIO = 35f / 40f;

    /**
     * The ratio for the size of the round corners.
     */
    private final static float HEIGHT_RATIO = 40f / 50f;

    /**
     * The ratio between width and height.
     */
    private final static float WIDTH_HEIGHT = 50f / 40f;

    /**
     * The full width of the node.
     */
    private float nodeWidth = 40;

    /**
     * The mid width of the node.
     */
    private float nodeMidWidth = 35;

    /**
     * The default height of the node.
     */
    private float nodeHeight = 50;

    /**
     * The default mid height of the node.
     */
    private float nodeMidHeight = 40;

    /**
     * Parent processing applet.
     */
    private final VizPanel parent;

    /**
     * X,Y positions on screen.
     */
    private float posX, posY;

    /**
     * The color of the node.
     */
    private int colorInt, colorExt, colorFont;

    /**
     * Stroke Widths for the node and the concentric rings.
     */
    private int nodeStrokeWidth, ringStrokeWidth;

    /**
     * The alpha value for coloring the internal parts of the node.
     */
    private int alphaInt;

    /**
     * The alpha value for coloring the border of the internal parts of the node.
     */
    private int alphaStroke;

    /**
     * The alpha value for coloring the concentric ring.
     */
    private int alphaExt;

    /**
     * The size of the concentric ring.
     */
    private float scale;

    /**
     * The identity of the node.
     */
    private final long id;

    /**
     * The identity of the node in Hex.
     */
    protected String hexId;

    /**
     * If this node is a cluster head or not.
     */
    private boolean clusterHead;

    /**
     * The sensor value.
     */
    private String sensorValue;
    /**
     * The semantic groups.
     */
    private HashMap<String, String> semanticGroups;

    /**
     * The sensor value.
     */
    private String aggregatedValue;

    /**
     * Map of the outgoing links per destination node.
     */
    private HashMap<Long, VizLink> outLinks;

    /**
     * If the node is enabled or not.
     */
    private boolean isEnabled;

    /**
     * The number of messages stored in the message buffer of the E2E module.
     */
    private int bufferSize;

    /**
     * The pre-installed keys of the node.
     */
    private SortedSet<Integer> keys;

    /**
     * The position of the keys-label.
     */
    private int posKeyLabel;

    /**
     * Default constructor.
     *
     * @param panel     -- the processing panel where to draw the node.
     * @param identity  -- the identity of the node.
     * @param initX     -- the initial position of the node on the X-axis.
     * @param initY     -- the initial position of the node on the Y-axis.
     * @param size      -- the initial size of the node.
     * @param thisColor -- the internal color of the node.
     * @param thatColor -- the external color of the node.
     * @param fontColor -- the external color of the font.
     */
    VizNode(final VizPanel panel,
            final long identity,
            final float initX,
            final float initY,
            final int size,
            final int thisColor,
            final int thatColor,
            final int fontColor) {
        parent = panel;
        id = identity;
        hexId = "0x" + Long.toHexString(id);
        posX = initX;
        posY = initY;
        colorInt = thisColor;
        colorExt = thatColor;
        colorFont = fontColor;
        alphaInt = 150;
        alphaStroke = 250;
        sensorValue = "";
        semanticGroups = new HashMap<String, String>();
        aggregatedValue = "";
        isEnabled = true;
        bufferSize = 0;

        // Fix node size
        nodeWidth = size;
        nodeMidWidth = nodeWidth * WIDTH_RATIO;
        nodeHeight = size * WIDTH_HEIGHT;
        nodeMidHeight = nodeHeight * HEIGHT_RATIO;

        nodeStrokeWidth = size / 3;
        ringStrokeWidth = size / 3;

        clusterHead = false;

        // Initialize outgoing links
        outLinks = new HashMap<Long, VizLink>();
    }

    /**
     * Get the identity of the node.
     *
     * @return the identity of the node.
     */
    public long getId() {
        return id;
    }

    /**
     * Get the identity of the node.
     *
     * @return the identity of the node in hex.
     */
    public String getHexId() {
        return hexId;
    }

    /**
     * The horizontal position of the node.
     *
     * @return the position on the X-axis.
     */
    public float getPosX() {
        return posX;
    }

    /**
     * Set the horizontal position of the node.
     *
     * @param thisX -- the position on the X-axis.
     */
    public void setPosX(final float thisX) {
        posX = thisX;
    }

    /**
     * The vertical position of the node.
     *
     * @return the position on the Y-axis.
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Set the vertical position of the node.
     *
     * @param thisY -- the position on the Y-axis.
     */
    public void setPosY(final float thisY) {
        posY = thisY;
    }

    /**
     * The internal color in RGB.
     *
     * @return the RGB color used to fill the node.
     */
    public int getColorInt() {
        return colorInt;
    }

    /**
     * Set the internal color used to fill the node.
     *
     * @param colorInt the RGB color used to fill the node.
     */
    public void setColorInt(final int colorInt) {
        this.colorInt = colorInt;
    }

    /**
     * Set the color used for the text labels.
     *
     * @param colorFont the RGB color used for the text labels.
     */
    public void setColorFont(final int colorFont) {
        this.colorFont = colorFont;
    }

    /**
     * Get the node size.
     *
     * @return the node size.
     */
    public float getNodeWidth() {
        return nodeWidth;
    }

    /**
     * Set the node size.
     *
     * @param size the node size.
     */
    public void setNodeWidth(final int size) {
        nodeWidth = size;
        nodeMidWidth = nodeWidth * WIDTH_RATIO;
        nodeHeight = size * WIDTH_HEIGHT;
        nodeMidHeight = nodeHeight * HEIGHT_RATIO;

        nodeStrokeWidth = size / 3;
        ringStrokeWidth = size / 3;
    }

    public void setSensorValue(final String value) {
        this.sensorValue = value;
    }

    public void addSemanticGroup(final String group, final String gparent) {
        final String semantic = group.substring(0, group.indexOf("-"));
        final String groupid = group.substring(group.indexOf("-"));
        this.semanticGroups.put(semantic, groupid + ":" + gparent);
    }

    public void setAggregatedValue(final String value) {
        this.aggregatedValue = value;
    }

    /**
     * Check if the node is a cluster head.
     *
     * @return true if the node is a cluster head.
     */
    public boolean isClusterHead() {
        return clusterHead;
    }

    /**
     * Set if the node is cluster head.
     *
     * @param isCHead true if the node is cluster head.
     */
    public void setClusterHead(final boolean isCHead) {
        clusterHead = isCHead;
        /*if (isCHead) {
            alphaInt = 200;
            alphaStroke = 255;
            nodeStrokeWidth = (int) nodeWidth;

        } else {
            alphaInt = 100;
            alphaStroke = 205;
            nodeStrokeWidth = (int) nodeWidth / 3;
        }
        */
    }

    /**
     * Add a new outgoing link.
     *
     * @param destID the destination ID.
     * @param link   the actual link.
     */
    public void addLink(final long destID, final VizLink link) {
        outLinks.put(destID, link);
    }

    /**
     * Remove an outgoing link.
     *
     * @param destID the destination ID.
     */
    public void removeLink(final long destID) {
        outLinks.remove(destID);
    }

    public VizLink getLink(final long destID) {
        return outLinks.get(destID);
    }

    public Collection<VizLink> getLinks() {
        return outLinks.values();
    }

    public void clearLinks() {
        outLinks.clear();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean value) {
        isEnabled = value;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public SortedSet<Integer> getKeys() {
        return keys;
    }

    public void setKeys(SortedSet<Integer> keys) {
        this.keys = keys;
    }

    public int getPosKeyLabel() {
        return posKeyLabel;
    }

    public void setPosKeyLabel(int posKeyLabel) {
        this.posKeyLabel = posKeyLabel;
    }

    /**
     * Add a new particle representing an incoming packet.
     *
     * @param thisLink -- the link that will be used to exchange the packet.
     * @param type     -- the type of the packet.
     */
    public void sendPacket(final VizLink thisLink, final int type, final int length, final String contents, final VizNode src, final VizNode tgt) {
        // Calculate offset
        float offSetY = 3f;
        if (src.getId() == thisLink.getSource().getId()) {
            offSetY = -10f;
        }

        if (thisLink.getType() == VizLink.LINK_UNI) {
            return;
        }

        if (thisLink.isEnabled()) {
            parent.setPacket(this, thisLink, type, length, contents, offSetY);
        }
    }

    /**
     * Add a new particle representing an incoming packet.
     *
     * @param thisLink -- the link that will be used to exchange the packet.
     * @param type     -- the type of the packet.
     */
    public void sendPacket(final VizLink thisLink, final int type, final int length, final VizNode src, final VizNode tgt) {
        sendPacket(thisLink, type, length, null, src, tgt);
    }

    /**
     * Draw the node.
     */
    void display() {
        if (isEnabled()) {
            parent.strokeCap(PApplet.ROUND);

            // draw outer frame for events
            //displayOvalRing();
            displayRoundRing();

            // draw inner frame
            //displayOvalNode();
            displayRoundNode();
        }
    }

    /**
     * Display the node.
     */
    void displayRoundNode() {
        parent.pushMatrix();
        parent.translate(posX, posY);
        parent.strokeWeight(0);

        parent.fill(Color.BLACK.getRGB(), 255);
        parent.ellipse(0, 0, nodeWidth, nodeWidth);

        parent.fill(colorInt, alphaInt);
        parent.ellipse(0, 0, nodeWidth, nodeWidth);

        parent.stroke(colorInt, alphaStroke);
        parent.ellipse(0, 0, nodeWidth + 15, nodeWidth + 15);

        if (clusterHead) {
            parent.ellipse(0, -nodeWidth + 30, 15, 15);
        }

        parent.fill(colorFont, 255);
        parent.textSize(12);
        parent.textAlign(PApplet.CENTER);
        parent.text(hexId, 0, nodeWidth + 5);

        parent.textSize(16);
        parent.textAlign(PApplet.LEFT);
        parent.text(sensorValue, nodeWidth - 30, 16);

        if (!semanticGroups.isEmpty()) {
            final StringBuilder totalsems = new StringBuilder();
            for (final String sems : semanticGroups.keySet()) {
                totalsems.append(sems);
                totalsems.append(semanticGroups.get(sems));
                totalsems.append("\n");

            }

            parent.textSize(12);
            parent.textAlign(PApplet.LEFT);
            parent.text(totalsems.toString(), nodeWidth - 30, 16);
        }


        parent.fill(Color.GREEN.getRGB(), 255);
        parent.textSize(16);
        parent.textAlign(PApplet.LEFT);
        parent.text(aggregatedValue, nodeWidth - 30, -5);

        // Display packets stored in buffer
        for (int packet = 0; packet < bufferSize; packet++) {
            parent.fill(Color.RED.getRGB(), 255);
            parent.rect(55 + (packet % 5) * 7, 20 + (packet / 5) * 7, 4, 4);
        }

        // Display private keys
        parent.textSize(12);
        parent.textAlign(PApplet.LEFT);
        parent.fill(Color.WHITE.getRGB(), 255);
        if (keys != null) {
            int startPosX = 55;
            int startPosY = 20;

            switch (getPosKeyLabel()) {
                case 1: // Top-right
                    startPosX = 55;
                    startPosY = -55;
                    break;

                default: // Bottom-right
                    startPosX = 55;
                    startPosY = 20;
            }

            int keyCnt = 0;
            int hskip = startPosX;
            for (Integer key : keys) {
                parent.text(key, hskip, startPosY + (keyCnt / 5) * 14);

                keyCnt++;
                if (keyCnt % 5 == 0) {
                    hskip = startPosX;
                } else {
                    if (key < 10) {
                        hskip += 18;
                    } else if (key < 100) {
                        hskip += 18;
                    } else {
                        hskip += 25;
                    }

                }
            }
        }

//        parent.strokeWeight(3);
//        parent.line(-120, 0, 120, 0);
//        parent.line(0, -120, 0, 120);

        parent.popMatrix();
    }

    /**
     * Display the surrounding ring for transmit events.
     */
    void displayRoundRing() {
        parent.fill(colorInt, alphaExt);
        parent.stroke(colorInt, alphaExt);
        parent.pushMatrix();
        parent.translate(posX, posY);
        parent.scale(scale);
        parent.ellipse(0, 0, nodeWidth + 10, nodeWidth + 10);
        parent.popMatrix();
    }

    /**
     * Draw the actual shape.
     */
    void displayRoundShape() {
        parent.beginShape();
        parent.vertex(0, -nodeHeight);
        parent.bezierVertex(nodeMidWidth, -nodeHeight, nodeWidth, -nodeMidHeight, nodeWidth, 0);
        parent.bezierVertex(nodeWidth, nodeMidHeight, nodeMidWidth, nodeHeight, 0, nodeHeight);
        parent.bezierVertex(-nodeMidWidth, nodeHeight, -nodeWidth, nodeMidHeight, -nodeWidth, 0);
        parent.bezierVertex(-nodeWidth, -nodeMidHeight, -nodeMidWidth, -nodeHeight, 0, -nodeHeight);
        parent.endShape();
    }

    /**
     * Display the node.
     */
    void displayOvalNode() {
        parent.pushMatrix();
        parent.translate(posX, posY);
        parent.fill(Color.BLACK.getRGB(), 255);
        displayOvalShape();

        parent.fill(colorInt, alphaInt);
        parent.stroke(colorInt, alphaStroke);
        parent.strokeWeight(nodeStrokeWidth);
        displayOvalShape();

        parent.fill(colorFont, 255);
        parent.textAlign(PApplet.CENTER);
        parent.text(hexId, 0, nodeHeight + 30);

        parent.textAlign(PApplet.LEFT);
        parent.text(sensorValue, nodeWidth + 10, nodeHeight / 2 - 10);

        parent.popMatrix();
    }

    /**
     * Display the surrounding ring for transmit events.
     */
    void displayOvalRing() {
        parent.fill(colorExt, alphaExt);
        parent.stroke(colorExt, alphaExt);
        parent.strokeWeight(ringStrokeWidth);
        parent.pushMatrix();
        parent.translate(posX, posY);
        parent.scale(scale);
        displayOvalShape();
        parent.popMatrix();
    }

    /**
     * Draw the actual shape.
     */
    void displayOvalShape() {
        parent.beginShape();
        parent.vertex(0, -nodeHeight);
        parent.bezierVertex(nodeMidWidth, -nodeHeight, nodeWidth, -nodeMidHeight, nodeWidth, 0);
        parent.bezierVertex(nodeWidth, nodeMidHeight, nodeMidWidth, nodeHeight, 0, nodeHeight);
        parent.bezierVertex(-nodeMidWidth, nodeHeight, -nodeWidth, nodeMidHeight, -nodeWidth, 0);
        parent.bezierVertex(-nodeWidth, -nodeMidHeight, -nodeMidWidth, -nodeHeight, 0, -nodeHeight);
        parent.endShape();
    }

    /**
     * Reset the counters when a new transmit event is fired.
     */
    public void ucastEvent() {
        alphaExt = 250;
        scale = 1.1f;
    }


    /**
     * Reset the counters when a new transmit event is fired.
     */
    public void bcastEvent() {
        bcastEvent(colorExt, 2, null);
    }

    /**
     * Reset the counters when a new transmit event is fired.
     *
     * @param contents the contents of the packet.
     */
    public void bcastEvent(final int color, final int length, final String contents) {
        alphaExt = 250;
        scale = 1.1f;

        synchronized (parent) {
            // Transmit a new packet over each outgoing link
            for (final VizLink thisLink : outLinks.values()) {
                final VizNode srcNode = thisLink.getSource();
                final VizNode tgtNode = thisLink.getTarget();
                /*if (srcNode.getId() == this.getId()) {
                    sendPacket(thisLink, colorExt, 2, srcNode, tgtNode);

                } else */
                if (thisLink.getType() != VizLink.LINK_UNI) {
                    sendPacket(thisLink, color, length, contents, srcNode, tgtNode);
                }
            }
        }
    }

    /**
     * Reduce the counters as the time passes by.
     *
     * @param value     the alphaExt value for the color of the concentric ring.
     * @param thatValue the scale of the concentric ring.
     */
    public void tick(final int value, final float thatValue) {
        if (alphaExt >= value) {
            alphaExt -= value;
            scale += thatValue;

        } else {
            alphaExt = 0;
        }
    }

    protected VizPanel getParent() {
        return parent;
    }
}
