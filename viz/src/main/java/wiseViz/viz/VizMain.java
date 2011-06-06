package wizeViz.viz;

import wizeViz.viz.base.VizPanel;
import wizeViz.viz.parsers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;

/**
 * Main class for instantiating the visualization panel.
 */
public class VizMain extends JFrame {


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
    public VizMain(final String path, final int delay, final String propertyFile) {
        super("FRONTS Vizualizer");


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
        //setSize(dim.width, dim.height);
        //setSize(1410, 864);
//        setSize(dim.width, dim.height);
        setSize(2096,768);

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
            lproc.addObserver(new NodeEnable(panel));
            lproc.addObserver(new NodeDisable(panel));
            lproc.addObserver(new TimestampParser(panel));
            lproc.addObserver(new NeighborhoodModule(panel));
            lproc.addObserver(new ClusteringModule(panel));
            lproc.addObserver(new ClusterRadioModule(panel));
            lproc.addObserver(new ClusteringMessages(panel));
            lproc.addObserver(new ClusterRadioMessages(panel));
            lproc.addObserver(new E2EModule(panel));
            lproc.addObserver(new E2ECache(panel));
            lproc.addObserver(new SensorValue(panel));
            lproc.addObserver(new AggregationModule(panel));
            lproc.addObserver(new AggregationMessages(panel));
            lproc.addObserver(new AppRadioReceiveMessages(panel));
            lproc.addObserver(new AppRadioMessages(panel));
            lproc.addObserver(new GKEInstalledKeys(panel));
            lproc.addObserver(new GKESecureLinks(panel));
            lproc.addObserver(new GKEClustering(panel));
            lproc.addObserver(new GKEAppMessages(panel));
            lproc.addObserver(new GKEStatMessage(panel));	// WROC hack :]


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
        new VizMain(args[0], Integer.parseInt(args[1]), args[2]);
    }

}
