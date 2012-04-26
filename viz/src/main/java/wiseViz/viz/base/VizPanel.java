package wiseViz.viz.base;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import wiseViz.viz.VizProperties;
import wiseViz.viz.tasks.PulseNodeEvents;
import wiseViz.viz.tasks.PulsePacketEvents;

import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * Sophisticated Front End for the FRONTS unified experiment.
 */
public final class VizPanel extends PApplet {

    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 42L; //NOPMD

    /**
     * Rate of color flushing of node events.
     */
    public static final int FLUSH_SPEED_ND = 50;

    /**
     * Rate of color flushing of link events.
     */
    public static final int FLUSH_SPEED_LNK = 100;

    /**
     * Rate of color flushing of packet events.
     */
    public static final int FLUSH_SPEED_PKT = 3;

    /**
     * Rate of color flushing of packet events.
     */
    public static final int FLUSH_ND_MOD = 2000;

    /**
     * The initial size of the nodes.
     */
    public static final int DEFAULT_NODE_SIZE = 3;

    /**
     * The node size used.
     */
    public int nodeSize;

    /**
     * The arduino node size used.
     */
    public int arduinoSize;

    /**
     * Font for printing the node Ids.
     */
    private final PFont font = createFont("Verdana", 16);

    /**
     * Background image.
     */
    private PImage bgmap;

    /**
     * Collection of background images.
     */
    private final Map<String, PImage> bgmaps;

    /**
     * The x and y coordinates of the image.
     */
    private int offsetX, offsetY;

    private float origX, origY;

    private float scaleX, scaleY;

    /**
     * List of network nodes.
     */
    private final ArrayList<VizNode> nodesList;

    /**
     * List of network nodes.
     */
    private final ArrayList<VizArduinoNode> arduinoList;

    /**
     * Map of network nodes.
     */
    private final Map<Long, VizNode> nodes;

    /**
     * List of network links.
     */
    private final Map<String, VizLink> links;

    /**
     * Frames change timer.
     */
    private final Timer timer = new Timer();

    /**
     * Flag to signal the display of the bg map.
     */
    private boolean showBGMap;

    /**
     * Currently Selected Node.
     */
    private VizNode selectedNode;

    /**
     * Last date time tag.
     */
    private String lastDateTag;

    /**
     * Associate each packet particle with a link.
*     */
    private final ArrayList<VizPacket> packetList;

    private boolean enableCam = false;

    private int selectedNodePos;

    private int selectedArduinoPos;

    private float prevX, prevY, stepChange;

    /**
     * Default constructor.
     *
     * @param width  the width of the panel.
     * @param height the height of the panel.
     */
    @SuppressWarnings("unchecked")
    public VizPanel(final int width, final int height) {
        screen.width = width;
        screen.height = height;

        // Setup nodes & links
        nodes = new TreeMap<Long, VizNode>();
        nodesList = new ArrayList<VizNode>();
        packetList = new ArrayList<VizPacket>();
        arduinoList = new ArrayList<VizArduinoNode>();
        bgmaps = new HashMap<String, PImage>();

        links = new HashMap<String, VizLink>();
        lastDateTag = "";
        selectedNodePos = -1;
        selectedArduinoPos = -1;
        prevX = 0;
        prevY = 0;
        stepChange = 5;

        selectedNode = null;

        showBGMap = VizProperties.getInstance().getProperty(VizProperties.MAP_ENABLE, false);

        // Load background image
        if ((showBGMap) && (VizProperties.getInstance().getProperty(VizProperties.MAP_FILE, "").length() > 0)) {
            bgmap = loadImage(VizProperties.getInstance().getProperty(VizProperties.MAP_FILE, ""));
        }

        // Load collection of background images
        if (VizProperties.getInstance().getProperty(VizProperties.LOOP_IMAGES, "").endsWith("/")) {
            final File path = new File(VizProperties.getInstance().getProperty(VizProperties.LOOP_IMAGES, ""));
            for (File file : path.listFiles()) {
                System.out.println(file.getName());
                bgmaps.put(file.getName(), loadImage(file.getAbsolutePath()));
            }
        }

        nodeSize = VizProperties.getInstance().getProperty(VizProperties.NODE_SIZE, DEFAULT_NODE_SIZE);
        arduinoSize = VizProperties.getInstance().getProperty(VizProperties.ARDUINO_SIZE, DEFAULT_NODE_SIZE);

        // try to load original size of screen
        String strPos = VizProperties.getInstance().getProperty(VizProperties.SCREEN_SIZE);
        if (strPos != null) {
            StringTokenizer stok = new StringTokenizer(strPos, ",");
            origX = Float.parseFloat(stok.nextToken());
            origY = Float.parseFloat(stok.nextToken());
        } else {
            origX = width;
            origY = height;
        }
    }

    /**
     * Setup processing.
     */
    public void setup() {
        // Create Environment.
        size(2048, 768, JAVA2D);
        textMode(MODEL);
        background(255);
        noStroke();
        smooth();
        frameRate(30);

        // Set the default font
        textFont(font, 255);
        textSize(16);

        // Setup Background image
        setupBGMap();

        // Add periodic tasks
        setupTasks();
    }

    private void setupBGMap() {
        // resize bg image
        if (showBGMap) {
            bgmap.resize(screen.width, 0);
            if (bgmap.height > screen.height) {
                bgmap.resize(0, screen.height);
            }

            scaleX = (float) bgmap.width / origX;
            scaleY = (float) bgmap.height / origY;

            offsetX = (screen.width - bgmap.width) / 2;
            offsetY = (screen.height - bgmap.height) / 2;

        } else {
            scaleX = screen.width / origX;
            scaleY = screen.height / origY;
            offsetX = 0;
            offsetY = 0;
        }
    }

    private void setupTasks() {
        try {
            // Decay events
            timer.scheduleAtFixedRate(new PulseNodeEvents(this), FLUSH_SPEED_ND, FLUSH_SPEED_ND);
            //timer.scheduleAtFixedRate(new PulseLinkEvents(this), FLUSH_SPEED_LNK, FLUSH_SPEED_LNK);

            // Send ND packets
            timer.scheduleAtFixedRate(new PulsePacketEvents(this), FLUSH_SPEED_PKT, FLUSH_SPEED_PKT);

            // Process camera
            //timer.scheduleAtFixedRate(new OpenCVTask(this), 3000, FLUSH_SPEED_LNK);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Keep the motor running... draw() needs to be added in auto mode, even if it is empty to keep things rolling.
     */
    public void draw() {
        if (showBGMap) {
            background(255);
            fill(0);
            image(bgmap, offsetX, offsetY);
        } else {
            background(0);
            fill(255);
        }

        // Draw Date & Time of last tag
        textAlign(RIGHT);
        textSize(18);
        fill(255);
        text(lastDateTag, 100, 30);
        textSize(16);

        // Draw the network elements
        drawNetwork();
    }

    /**
     * Draw all the nodes and links.
     */
    public void drawNetwork() {
        synchronized (packetList) {
            for (VizPacket packet : packetList) {
                if (packet.getLink().isEnabled()) {
                    packet.display();
                }
            }
        }

        synchronized (links) {
            for (VizLink link : links.values()) {
                link.display();
            }
        }

        synchronized (nodes) {
            for (final VizNode node : nodes.values()) {
                node.display();
            }
        }
    }

    /**
     * Advance the events of all nodes.
     *
     * @param value     the rate of decay for the alpha color.
     * @param thatValue the rate of decay for the size.
     */
    public void tickNodes(final int value, final float thatValue) {
        synchronized (nodes) {
            for (final VizNode node : nodes.values()) {
                node.tick(value, thatValue);
            }
        }
    }

    /**
     * Advance the events of all links.
     *
     * @param value the rate of decay for the alpha color.
     */
    public void tickLinks(final int value) {
        synchronized (links) {
            for (final VizLink link : links.values()) {
                link.tick(value);
            }
        }
    }

    /**
     * Advance the events of all packets.
     */
    public void tickPackets() {
        synchronized (packetList) {
            ArrayList<VizPacket> oldPackets = new ArrayList<VizPacket>();
            for (VizPacket packet : packetList) {
                packet.tick();
                if (packet.isDelivered()) {
                    oldPackets.add(packet);
                }
            }

            // Remove delivered packets
            for (VizPacket packet : oldPackets) {
                packetList.remove(packet);
            }
        }
    }

    public void setPacket(final VizNode src, final VizLink link, final int color, final int length, final String contents, final float offSetY) {
        synchronized (packetList) {
//            if (link.getType() != VizLink.LINK_UNI) {
            packetList.add(new VizPacket(this, link, src, offSetY, length, color, contents));
//            }
        }
    }

    /**
     * Adds a new node in the network.
     *
     * @param identity  -- the identity of the node.
     * @param initX     -- the initial position of the node on the X-axis.
     * @param initY     -- the initial position of the node on the Y-axis.
     * @param thisColor -- the internal color of the node.
     * @param thatColor -- the external color of the node.
     * @param fontColor -- the external color of the font.
     * @return the VizNode representation.
     */
    public final VizNode displayNode(final long identity,
                                     final float initX,
                                     final float initY,
                                     final int thisColor,
                                     final int thatColor,
                                     final int fontColor) {
        VizNode node = new VizNode(this, identity, initX, initY,
                nodeSize,
                thisColor,  // Inner color
                thatColor, // Send Msg color
                fontColor); // Font color

        // try to load additional node properties
        int posKeyLabel = VizProperties.getInstance().getProperty(VizProperties.NODE_KEYLABEL + "0x" + Long.toHexString(identity), 0);
        node.setPosKeyLabel(posKeyLabel);

        synchronized (nodes) {
            nodes.put(identity, node);

            nodesList.clear();
            for (VizNode vizNode : nodes.values()) {
                nodesList.add(vizNode);
            }
        }

        //setupArduinoLinks(node);

//        synchronized (timer) {
//            try {
//                timer.scheduleAtFixedRate(new NodeTransmitEvent(node), 0, FLUSH_ND_MOD + ((int) random(100)));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        }

        node.bcastEvent();
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
        final String linkID1 = srcNode.getId() + "." + tgtNode.getId();
//        final double linkID2 = Double.parseDouble(tgtNode.getId() + "." + srcNode.getId());
        VizLink link = null;

        if ((srcNode.getId() == 0) || (tgtNode.getId() == 0)) {
            return link;
        }

        // Check if this link is already displayed
        synchronized (links) {
            if (links.containsKey(linkID1)) {
                // already exists
                link = links.get(linkID1);
                link.setType(linkType);
                link.fireEvent();

//            } else if (links.containsKey(linkID2)) {
//                // already exists
//                link = links.get(linkID2);
//                link.setType(linkType);
//                link.fireEvent();

            } else {
                link = new VizLink(this, srcNode, tgtNode, linkType);
                links.put(linkID1, link);
                // don't store GKE_SL links in node's queue for sending msgs!
                if (linkType != VizLink.LINK_SL) {
                    srcNode.addLink(tgtNode.getId(), link);
                    tgtNode.addLink(srcNode.getId(), link);
                }
            }
        }
        return link;
    }

    /**
     * Adds a new link in the network.
     *
     * @param srcNode the source Node.
     * @param tgtNode the target Node.
     */
    public void removeLink(final VizNode srcNode, final VizNode tgtNode) {
        final String linkID1 = srcNode.getId() + "." + tgtNode.getId();
        //final double linkID2 = Double.parseDouble(tgtNode.getId() + "." + srcNode.getId());

        // Check if this link is already displayed
        synchronized (links) {
            if (links.containsKey(linkID1)) {
                // already exists
                links.remove(linkID1);
                srcNode.removeLink(tgtNode.getId());
                tgtNode.removeLink(srcNode.getId());

/*            } else if (links.containsKey(linkID2)) {
                // already exists
                links.remove(linkID2);
                srcNode.removeLink(tgtNode.getId());
                tgtNode.removeLink(srcNode.getId());
                */
            }
        }
    }

    public boolean isShowBGMap() {
        return showBGMap;
    }

    /**
     * Get the total number of nodes registed.
     *
     * @return the number of nodes displayed.
     */
    public int getNodesSize() {
        synchronized (nodes) {
            return nodes.size();
        }
    }

    /**
     * Retrieve the particular arduino node.
     *
     * @param nodeId the identity of the node to retrieve.
     * @return the VizNode that correspond to the given nodeID.
     */
    public VizNode getArduinoNode(final long nodeId) {
        synchronized (arduinoList) {
            return arduinoList.get((int) nodeId);
        }
    }

    /**
     * Retrieve the particular node.
     *
     * @param nodeId the identity of the node to retrieve.
     * @return the VizNode that correspond to the given nodeID.
     */
    public VizNode getNode(final long nodeId) {
        synchronized (nodes) {
            return nodes.get(nodeId);
        }
    }

    /**
     * Examines if the given nodeId is registered..
     *
     * @param nodeId the identity of the node to retrieve.
     * @return true if it is registered, otherwise false.
     */
    public boolean containsNode(final long nodeId) {
        synchronized (nodes) {
            return nodes.containsKey(nodeId);
        }
    }

    /**
     * Monitor keyboard for testing purposes.
     */
    public void keyPressed() {
        switch (key) {
            case 'm':
                // Toggle display of background map
                showBGMap = !showBGMap;
                int fontColor = Color.WHITE.getRGB();
                if (showBGMap) {
                    fontColor = Color.BLACK.getRGB();
                }
                synchronized (nodes) {
                    for (final VizNode node : nodes.values()) {
                        node.setColorFont(fontColor);
                    }
                }
                VizProperties.getInstance().setProperty(VizProperties.MAP_ENABLE, showBGMap);
                break;


            case 's':
                // Save the properties
                try {
                    VizProperties.getInstance().save();
                } catch (Exception ex) {
                    System.err.println("Failed to write to property file.");
                }

                break;


            case 'c':
                // Toggle camera
                enableCam = !enableCam;
                break;

            case 43: // Plus size
                nodeSize++;
                VizProperties.getInstance().setProperty(VizProperties.NODE_SIZE, nodeSize);

                // increase node size
                synchronized (nodes) {
                    for (VizNode vizNode : nodes.values()) {
                        vizNode.setNodeWidth(nodeSize);
                    }
                }
                break;

            case 45: // Reduce size
                nodeSize--;
                VizProperties.getInstance().setProperty(VizProperties.NODE_SIZE, nodeSize);

                // decrease node size
                synchronized (nodes) {
                    for (VizNode vizNode : nodes.values()) {
                        vizNode.setNodeWidth(nodeSize);
                    }
                }
                break;

            case '0': // Plus size
                arduinoSize++;
                VizProperties.getInstance().setProperty(VizProperties.ARDUINO_SIZE, arduinoSize);

                // increase node size
                synchronized (arduinoList) {
                    for (VizNode vizNode : arduinoList) {
                        vizNode.setNodeWidth(arduinoSize);
                    }
                }
                break;

            case '9': // Reduce size
                arduinoSize--;
                VizProperties.getInstance().setProperty(VizProperties.ARDUINO_SIZE, arduinoSize);

                // decrease node size
                synchronized (arduinoList) {
                    for (VizNode vizNode : arduinoList) {
                        vizNode.setNodeWidth(arduinoSize);
                    }
                }
                break;


            case CODED:
                if (selectedNode != null) {
                    switch (keyCode) {
                        case UP:
                            selectedNode.setPosY(selectedNode.getPosY() - stepChange);
                            break;

                        case DOWN:
                            selectedNode.setPosY(selectedNode.getPosY() + stepChange);
                            break;

                        case LEFT:
                            selectedNode.setPosX(selectedNode.getPosX() - stepChange);
                            break;

                        case RIGHT:
                            selectedNode.setPosX(selectedNode.getPosX() + stepChange);
                            break;

                        default:
                            // do nothing
                    }

                    // make sure that the bg image is saved
                    if (bgmap != null) {
                        VizProperties.getInstance().setProperty(VizProperties.SCREEN_SIZE, bgmap.width + "," + bgmap.height);
                    } else {
                        VizProperties.getInstance().setProperty(VizProperties.SCREEN_SIZE, screen.width + "," + screen.height);
                    }

                    // Update position of the node
                    if (selectedNode instanceof VizArduinoNode) {
                        VizProperties.getInstance().setProperty(VizProperties.ARDUINO_POSITION + selectedNode.getId(),
                                selectedNode.getPosX() + "," + selectedNode.getPosY());
                    } else {
                        VizProperties.getInstance().setProperty(VizProperties.NODE_POSITION + selectedNode.getHexId(),
                                selectedNode.getPosX() + "," + selectedNode.getPosY());
                    }
                }
                break;

            case 'q':
                if (nodesList.size() > 0) {
                    if (nodesList.size() > selectedNodePos + 1) {
                        selectedNodePos++;
                        selectedNode = nodesList.get(selectedNodePos);
                        prevX = selectedNode.getPosX();
                        prevY = selectedNode.getPosY();
                        selectedNode.setPosX(screen.width / 4);
                        selectedNode.setPosY(screen.height / 2);
                    }
                }
                break;

            case 'w':
                if (nodesList.size() > 0) {
                    if ((nodesList.size() > 0) && (selectedNodePos > 0)) {
                        selectedNodePos--;
                        selectedNode = nodesList.get(selectedNodePos);
                        prevX = selectedNode.getPosX();
                        prevY = selectedNode.getPosY();
                        selectedNode.setPosX(screen.width / 4);
                        selectedNode.setPosY(screen.height / 2);
                    }
                }
                break;

            case 'e':
                if (selectedNode != null) {
                    selectedNode.setPosX(prevX);
                    selectedNode.setPosY(prevY);
                }
                break;

            case 'z':
                stepChange = 5;
                break;

            case 'x':
                stepChange = 1;
                break;

            case 'i':
                selectedNode = new VizArduinoNode(this, arduinoList.size(), screen.width / 4, screen.height / 2,
                        nodeSize, arduinoSize,
                        Color.RED.getRGB(), Color.RED.getRGB(), Color.WHITE.getRGB());
                arduinoList.add((VizArduinoNode) selectedNode);
                nodes.put(selectedNode.getId(), selectedNode);

                VizProperties.getInstance().setProperty(VizProperties.ARDUINO_COUNT, arduinoList.size());

                break;

            case 'o':
                if (arduinoList.size() > 0) {
                    if (arduinoList.size() > selectedArduinoPos + 1) {
                        selectedArduinoPos++;
                        selectedNode = arduinoList.get(selectedArduinoPos);
                        prevX = selectedNode.getPosX();
                        prevY = selectedNode.getPosY();
                        selectedNode.setPosX(screen.width / 4);
                        selectedNode.setPosY(screen.height / 2);
                    }
                }
                break;

            case 'p':
                if (arduinoList.size() > 0) {
                    if ((arduinoList.size() > 0) && (selectedArduinoPos > 0)) {
                        selectedArduinoPos--;
                        selectedNode = arduinoList.get(selectedArduinoPos);
                        prevX = selectedNode.getPosX();
                        prevY = selectedNode.getPosY();
                        selectedNode.setPosX(screen.width / 4);
                        selectedNode.setPosY(screen.height / 2);
                    }
                }
                break;

            case 'h':
                // This is only working for SPITFIRE demo
                bgmap = bgmaps.get("bigHouseT02-000000.png");
                break;

            case 'j':
                // This is only working for SPITFIRE demo
                bgmap = bgmaps.get("bigHouseT02-100000.png");
                break;

            case 'k':
                // This is only working for SPITFIRE demo
                bgmap = bgmaps.get("bigHouseT02-010000.png");
                break;

            case 'l':
                // This is only working for SPITFIRE demo
                bgmap = bgmaps.get("bigHouseT02-110000.png");
                break;

//            default:
//                if (key >= 65 && key <= 90) {
//                    if (key - 65 < nodesList.size()) {
//                        selectedNode = nodesList.get(key - 65);
//                        selectedNode.setColorInt(Color.ORANGE.getRGB());
//                    } else {
//                        selectedNode = null;
//                    }
//                } else if (key >= 48 && key <= 57) {
//                    if (25 + key - 48 < nodesList.size()) {
//                        selectedNode = nodesList.get(25 + key - 48);
//                        selectedNode.setColorInt(Color.ORANGE.getRGB());
//                    } else {
//                        selectedNode = null;
//                    }
//                }
//                break;
        }
    }

    public void setLastDateTag(final String tag) {
        lastDateTag = tag;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setBgImage(String filename) {
        bgmap=bgmaps.get(filename);
    }
}
