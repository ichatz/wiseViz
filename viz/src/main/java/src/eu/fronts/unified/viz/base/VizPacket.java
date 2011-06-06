package eu.fronts.unified.viz.base;

import processing.core.PApplet;

import java.awt.*;

/**
 * Represents a single packet.
 */
public class VizPacket {

    private final static float HEIGHT = 8f;

    private final static float SPEED = 1f;

    /**
     * Parent processing applet.
     */
    private final VizPanel parent;

    /**
     * X,Y positions on screen.
     */
    private float posX, posY, offSetY, posOnLink;

    /**
     * The color of the packet.
     */
    private int color;

    /**
     * The full width of the packet.
     */
    private float width = 4;

    /**
     * The link over which the packet flies.
     */
    private VizLink link;

    /**
     * If the packet is delivered.
     */
    private boolean delivered;

    private float length, angle;

    private String data;


    public VizPacket(final VizPanel panel,
                     final VizLink theLink,
                     final VizNode theSender,
                     final float offset,
                     final int size,
                     final int thisColor,
                     final String contents) {
        parent = panel;
        link = theLink;
        posX = theSender.getPosX();
        posY = theSender.getPosY();
        offSetY = offset;
        color = thisColor;
        width = size;
        posOnLink = 5;
        data = contents;

        delivered = false;

        length = link.getLength();

        // Calculate angle
        final VizNode target = (theSender == theLink.getSource()) ? (theLink.getTarget()) : (theLink.getSource());
        final float opposite = target.getPosY() - theSender.getPosY();
        angle = (float) Math.asin(opposite / length);

        if (target.getPosX() < theSender.getPosX()) {
            angle = parent.PI - angle;
        }
    }

    public VizLink getLink() {
        return link;
    }

    public void setLink(final VizLink value) {
        this.link = value;
    }

    /**
     * The horizontal position of the packet.
     *
     * @return the position on the X-axis.
     */
    public float getPosX() {
        return posX;
    }

    /**
     * Set the horizontal position of the packet.
     *
     * @param thisX -- the position on the X-axis.
     */
    public void setPosX(final float thisX) {
        posX = thisX;
    }

    /**
     * The vertical position of the packet.
     *
     * @return the position on the Y-axis.
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Set the vertical position of the packet.
     *
     * @param thisY -- the position on the Y-axis.
     */
    public void setPosY(final float thisY) {
        posY = thisY;
    }

    public float getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(final float value) {
        this.offSetY = value;
    }

    /**
     * The internal color in RGB.
     *
     * @return the RGB color used to fill the packet.
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the internal color used to fill the packet.
     *
     * @param colorInt the RGB color used to fill the packet.
     */
    public void setColor(final int colorInt) {
        this.color = colorInt;
    }

    /**
     * Get the packet size.
     *
     * @return the packet size.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Set the packet size.
     *
     * @param size the packet size.
     */
    public void setWidth(final int size) {
        width = size;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(final boolean value) {
        this.delivered = value;
    }

    /**
     * Draw the packet.
     */
    void display() {
        if (delivered) {
            return;
        }

        if (link.isEnabled() && link.getSource().isEnabled() && link.getTarget().isEnabled()) {
            parent.strokeCap(PApplet.ROUND);

            parent.fill(color);
            parent.stroke(color);

            parent.pushMatrix();
            parent.translate(getPosX(), getPosY());
            parent.rotate(angle);
            parent.rect(posOnLink, getOffSetY(), getWidth(), HEIGHT);

            if (data != null) {
                int posY = 10;
                parent.translate(posOnLink, getOffSetY());
                if (angle < PApplet.PI) {
                    parent.rotate(-0.5f*PApplet.PI);
                    parent.textAlign(PApplet.LEFT);
                    posY = 15;

                } else {
                    parent.rotate(0.5f*PApplet.PI);
                    parent.textAlign(PApplet.RIGHT);
                    posY = -10;
                }

                parent.fill(Color.WHITE.getRGB(), 255);
                parent.textSize(10);
                parent.text(data, 0, posY);
            }

            parent.popMatrix();
        }
    }

    /**
     * Advance position of packet.
     */
    public void tick() {
        posOnLink += SPEED;
        if (posOnLink > length) {
            delivered = true;
        }
    }

}
