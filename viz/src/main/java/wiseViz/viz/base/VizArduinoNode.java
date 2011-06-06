package wizeViz.viz.base;

import processing.core.PImage;

/**
 * Represents an arduino node.
 */
public final class VizArduinoNode extends VizNode {

    /**
     * Background image.
     */
    private PImage bgmap;

    private PImage imgUp, imgDown, imgFun, imgBulb, imgButton;

    private int arduinoSize;


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
    VizArduinoNode(final VizPanel panel,
                   final int identity,
                   final float initX,
                   final float initY,
                   final int size,
                   final int aSize,
                   final int thisColor,
                   final int thatColor,
                   final int fontColor) {
        super(panel, identity, initX, initY, size - 1, thisColor, thatColor, fontColor);
        arduinoSize = aSize;

        bgmap = panel.loadImage("src/eu/fronts/unified/viz/img/arduino-icon-small.png");
        bgmap.resize(arduinoSize, arduinoSize);

        switch (identity) {

            case 0:
                hexId = "0x8438";
                imgUp = panel.loadImage("src/eu/fronts/unified/viz/img/colorful_up.png");
                imgUp.resize(24, 24);
                imgDown = panel.loadImage("src/eu/fronts/unified/viz/img/colorful_down.png");
                imgDown.resize(24, 24);
                imgFun = panel.loadImage("src/eu/fronts/unified/viz/img/colorful_process.png");
                imgFun.resize(24, 24);
                imgBulb = panel.loadImage("src/eu/fronts/unified/viz/img/colorful_light_bulb.png");
                imgBulb.resize(24, 24);
                imgButton = panel.loadImage("src/eu/fronts/unified/viz/img/colorful_accept.png");
                imgButton.resize(24, 24);
                break;

            case 1:
                hexId = "0x8931";
                break;

            case 2:
                hexId = "0x6cb5";
                break;
        }
    }

    /**
     * Draw the node.
     */
    void display() {
        super.display();


        switch (getId()) {
            case 0:
                getParent().image(bgmap, getPosX() - 170, getPosY() - 30);
                getParent().image(imgBulb, getPosX() - 112, getPosY() - 45);
                getParent().image(imgFun, getPosX() - 112, getPosY() + 5);
                getParent().image(imgUp, getPosX() - 80, getPosY() - 45);
                getParent().image(imgDown, getPosX() - 80, getPosY() + 5);
                getParent().image(imgButton, getPosX() + 80, getPosY() - 15);
                break;

            case 1:
                getParent().image(bgmap, getPosX() + 50, getPosY() - 55);
                break;

            case 2:
                getParent().image(bgmap, getPosX() + 65, getPosY() - 18);
                break;
        }
    }

    /**
     * Set the node size.
     *
     * @param size the node size.
     */
    public void setNodeWidth(final int size) {
        arduinoSize = size;
        bgmap = getParent().loadImage("src/eu/fronts/unified/viz/img/arduino-icon-small.png");
        bgmap.resize(arduinoSize, arduinoSize);
    }

}
