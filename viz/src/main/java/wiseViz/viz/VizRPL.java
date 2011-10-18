package wiseViz.viz;

import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.tinyRPL.DAOPacketParser;
import wiseViz.viz.parsers.tinyRPL.DIOPacketParser;
import wiseViz.viz.parsers.tinyRPL.DISPacketParser;
import wiseViz.viz.parsers.tinyRPL.DataPacketParser;
import wiseViz.viz.parsers.tinyRPL.InitVizParser;
import wiseViz.viz.parsers.tinyRPL.TimestampParser;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.lang.Thread;

/**
 * Main class for instantiating the visualization panel of tinyRPL.
 */
public class VizRPL extends JFrame {

    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 42L; //NOPMD

    /**
     * Default constructor.
     *
     * @param path         the filename to parse.
     * @param delay        the delay in milliseconds after processing a trace line.
     * @param propertyFile the filename of the properties.
     */
    public VizRPL(final String path, final int delay, final String propertyFile) {
        super("tinyRPL Vizualizer");


        // Load property file
        VizProperties.getInstance().setPath(propertyFile);
        try {
            VizProperties.getInstance().load();
        } catch (Exception ex) {
            System.err.println("Could not read property file");
        }

        // Initialize VizPanel
        final int[] pixels = new int[16 * 16];
        final Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        final Cursor transparentCursor =
                Toolkit.getDefaultToolkit().createCustomCursor
                        (image, new Point(0, 0), "invisibleCursor");

        // Make mouse cursor transparent (i.e. hide)
        setCursor(transparentCursor);

        //Get Screen Size
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // make sure frame is maximized state
//        setExtendedState(Frame.MAXIMIZED_BOTH);

        // also remove any decorations
        setUndecorated(true);

        //Set default close operation on the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set Frames Dimensions
        setSize(dim.width, dim.height);

        //setLocation(-1366,0);

        //Set frame's visibility
        setVisible(true);

        //Set frames background Color
        getContentPane().setBackground(Color.black);

        // Processing panel for visual output.
        final VizPanel panel = new VizPanel(getWidth(), getHeight());
        panel.init();
        this.add(panel);

        // Read sample log file
        try {
            final LogObserver lproc = new LogObserver(path, delay);

            // Add trace file parsers
            lproc.addObserver(new InitVizParser(panel));
            lproc.addObserver(new TimestampParser(panel));
            lproc.addObserver(new DataPacketParser(panel));
            lproc.addObserver(new DIOPacketParser(panel));
            lproc.addObserver(new DISPacketParser(panel));
            lproc.addObserver(new DAOPacketParser(panel));

            final Thread thr = new Thread(lproc);
            thr.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main function.
     *
     * @param args String Argument
     */
    public static void main(final String[] args) {
        // Initialize vizualizer
            new VizRPL(args[0], Integer.parseInt(args[1]), args[2]);

    }


}
