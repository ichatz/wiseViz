package eu.fronts.unified.viz.tasks;

import eu.fronts.unified.viz.base.VizNode;
import eu.fronts.unified.viz.base.VizPanel;
import hypermedia.video.Blob;
import hypermedia.video.OpenCV;
import processing.core.PImage;

import java.awt.*;
import java.util.TimerTask;

/**
 * Used to control the camera.
 */
public class OpenCVTask extends TimerTask {

    /**
     * VizPanel to handle.
     */
    protected final VizPanel vpanel;

    /**
     * OpenCV object.
     */
    private OpenCV opencv = null;

    /**
     * Default constructor.
     *
     * @param thePanel the VizPanel to handle.
     */
    public OpenCVTask(final VizPanel thePanel) {
        vpanel = thePanel;

        // OpenCV setup
        opencv = new OpenCV(vpanel);
        opencv.capture(320, 240);
    }

    /**
     * Shake the words.
     */
    public void run() {
        try {
            processCamera();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void processCamera() {
        opencv.read();                              //  Captures a frame from the camera


        opencv.absDiff();                           //  Creates a difference image

        opencv.convert(OpenCV.GRAY);                //  Converts to greyscale
        opencv.blur(OpenCV.BLUR, 2);                //  Blur to remove camera noise
        opencv.threshold(32);                       //  Thresholds to convert to black and white

        final PImage frame = opencv.image();
        if (frame != null) {
            synchronized (vpanel) {
                vpanel.setCamera(opencv.image());
            }
        }

        opencv.remember(OpenCV.SOURCE);    //  Remembers the camera image so we can generate a difference image next frame. Since we've

        // find blobs
        Blob[] blobs = opencv.blobs(20, 60, 100, true, OpenCV.MAX_VERTICES * 4);

        System.out.println(blobs.length);

        // draw blobs
        for (Blob b : blobs) {

            // define blob's contour
            for (Point p : b.points) {
                VizNode node = vpanel.getArduinoNode(0);
                node.setPosX(p.x * vpanel.width / 320);
                node.setPosY(p.y * vpanel.height / 240);
            }
        }

    }

    public void processCamera2() {
        opencv.read();
        final PImage frame = opencv.image();
        if (frame != null) {
            synchronized (vpanel) {
                vpanel.setCamera(opencv.image());
            }
        }

        opencv.threshold(80);

        // find blobs
        Blob[] blobs = opencv.blobs(10, 200, 100, true, OpenCV.MAX_VERTICES * 4);

        // draw blob results
        for (int i = 0; i < blobs.length; i++) {
            for (int j = 0; j < blobs[i].points.length; j++) {
                vpanel.vertex(blobs[i].points[j].x, blobs[i].points[j].y);
            }
        }

//        trailsImg.blend(opencv.image(), 0, 0, 320, 240, 0, 0, 320, 240, SCREEN);  //  Blends the movement image with the trails image
//
//        colorMode(HSB);                 //  Changes the colour mode to HSB so that we can change the hue
//        tint(color(hCycle, 255, 255));  //  Sets the tint so that the hue is equal to hcycle and the saturation and brightness are at 100%
//        image(trailsImg, 0, 0);       //  Display the blended difference image
//        noTint();                       //  Turns tint off
//        colorMode(RGB);                 //  Changes the colour mode back to the default
//
//        opencv.copy(trailsImg);       //  Copies trailsImg into OpenCV buffer so we can put some effects on it
//        opencv.blur(OpenCV.BLUR, 4);  //  Blurs the trails image
//        opencv.brightness(-20);       //  Sets the brightness of the trails image to -20 so it will fade out
//        trailsImg = opencv.image();     //  Puts the modified image from the buffer back into trailsImg
//
//        opencv.remember();              //  Remembers the current frame
//
//        hCycle++;                       //  Increments the hCycle variable by 1 so that the hue changes each frame
//        if (hCycle < 255)
//            hCycle = 0;   //  If hCycle is greater than 255 (the maximum value for a hue) then make it equal to 0
    }
}

