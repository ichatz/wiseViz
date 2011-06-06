package eu.fronts.unified.viz.base;

import processing.core.PApplet;

import java.awt.*;

/**
 * Represents a network link.
 */
public final class VizLink {

    public final static int LINK_MSG_RCVD = 1;

    public final static int LINK_UNI = 2;

    public final static int LINK_BI = 3;

    public final static int LINK_SL = 4;

    public final static int LINK_CRADIO = 5;

    /**
     * Parent processing applet.
     */
    private final PApplet parent;

    /**
     * The source and target nodes.
     */
    private VizNode source, target;

    /**
     * The alpha for coloring the concentric ring.
     */
    private int alpha, minAlpha;

    /**
     * The type of the link.
     */
    private int type;

    /**
     * The angle of the link.
     */
    private float angle;

    /**
     * The length of the link.
     */
    private float length;

    /**
     * If the link is enabled or not.
     */
    private boolean isEnabled;

    /**
     * The key used for secure links.
     */
    private int key;

    /**
     * Default constructor.
     *
     * @param panel    -- the processing panel where to draw the node.
     * @param src      -- the source node.
     * @param tgt      -- the target node.
     * @param linkType -- the type of the link.
     */
    VizLink(final PApplet panel,
            final VizNode src,
            final VizNode tgt,
            final int linkType) {
        parent = panel;
        source = src;
        target = tgt;
        type = linkType;
        alpha = calcAlpha();
        minAlpha = calcMinAlpha();
        isEnabled = true;
        key = -1;

        // calculate angle
        length = (float) Math.sqrt(Math.pow((double) (src.getPosX() - tgt.getPosX()), 2d) + Math.pow((double) (src.getPosY() - tgt.getPosY()), 2d));
        float opposite = tgt.getPosY() - src.getPosY();
        angle = (float) Math.asin(opposite / length);

        if (target.getPosX() < source.getPosX()) {
            angle = parent.PI - angle;
        }
    }

    /**
     * Draw the node.
     */
    void display() {
        if (isEnabled() && source.isEnabled() && target.isEnabled()) {

            parent.strokeCap(PApplet.ROUND);

            // Check if this is a highway link
            switch (type) {
                case LINK_SL:
                    parent.stroke(new Color(184, 134, 11).getRGB(), alpha);
                    parent.strokeWeight(2);
                    break;

                case LINK_CRADIO:
                    parent.stroke(Color.GREEN.getRGB(), alpha);
                    parent.strokeWeight(2);
                    break;

                case LINK_BI:
                    parent.stroke(Color.GREEN.getRGB(), alpha);
                    parent.strokeWeight(2);
                    break;

                case LINK_UNI:
                    parent.stroke(Color.GREEN.getRGB(), alpha);
                    parent.strokeWeight(2);
                    return;

                case LINK_MSG_RCVD:
                default:
                    parent.stroke(Color.GREEN.getRGB(), alpha);
                    parent.strokeWeight(2);
                    break;
            }

            parent.line(source.getPosX(), source.getPosY(),
                    target.getPosX(), target.getPosY());

//            if (key > 0) {
//                parent.pushMatrix();
//                parent.translate(source.getPosX(), source.getPosY());
//                parent.rotate(angle);
//
//                parent.textSize(12);
//                parent.textAlign(PApplet.LEFT);
//                parent.fill(Color.WHITE.getRGB(), 255);
//                parent.text(key, 30, -50);
//
//                parent.popMatrix();
//            }
        }
    }

    /**
     * Set the type of the link.
     *
     * @param linkType -- the type of the link.
     */
    public void setType(final int linkType) {
        type = linkType;
        alpha = calcAlpha();
        minAlpha = calcMinAlpha();
    }

    /**
     * Get the type of the link.
     *
     * @return the type of the link.
     */
    public int getType() {
        return type;
    }

    /**
     * Get the source node of the link.
     *
     * @return the VizNode instance of the source node.
     */
    public VizNode getSource() {
        return source;
    }

    /**
     * Get the target node of the link.
     *
     * @return the VizNode instance of the target node.
     */
    public VizNode getTarget() {
        return target;
    }

    public float getAngle() {
        return angle;
    }

    public float getLength() {
        return length;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Reset the counters when a new transmit event is fired.
     */
    public void fireEvent() {
        alpha = calcAlpha();
    }

    /**
     * Reduce the counters as the time passes by.
     *
     * @param value the alpha value for the color of the concentric ring.
     */
    public void tick(final int value) {
        if (alpha > minAlpha + value) {
            alpha -= value;

        } else {
            alpha = minAlpha;
        }
    }

    /**
     * Calculate the Alpha Parameter depending on the type of the link.
     *
     * @return the Alpha to be used when drawing this link.
     */
    public int calcAlpha() {
        int thisAlpha;
        switch (type) {

            case LINK_SL:
            case LINK_CRADIO:
            case LINK_BI:
                thisAlpha = 250;
                break;

            case LINK_UNI:
                thisAlpha = 150;
                break;

            case LINK_MSG_RCVD:
            default:
                thisAlpha = 70;
                break;
        }

        return thisAlpha;
    }

    /**
     * Calculate the minimum Alpha parameter depending on the type of the link.
     *
     * @return the minimum value of the Alpha parameter when drawing this link.
     */
    public int calcMinAlpha() {
        int thisAlpha;
        switch (type) {

            case LINK_CRADIO:
            case LINK_SL:
                thisAlpha = 250;
                break;

            case LINK_BI:
                thisAlpha = 180;
                break;

            case LINK_UNI:
                thisAlpha = 70;
                break;

            case LINK_MSG_RCVD:
            default:
                thisAlpha = 5;
                break;
        }

        return thisAlpha;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean value) {
        isEnabled = value;
    }
}
