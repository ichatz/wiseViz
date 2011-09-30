/*
 * RealTimePlotsApp.java
 */

package wiseViz.plots;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The main class of the application.
 */
public class PlotsMain extends JFrame {

    private static Logger log = Logger.getLogger(PlotsMain.class);

    private ContentPanel cp;
    private java.util.List<AbstractParser> Parsers = new ArrayList<AbstractParser>();
    private Properties properties;

    public PlotsMain(final int delay, final String propertyFile) {
        super("wiseViz Plots");

        //logger configure
        BasicConfigurator.configure();

        properties = new Properties();
        try {
            properties.load(new FileInputStream(propertyFile));
        } catch (IOException e) {
            log.info("No properties file found! " + propertyFile + " not found!");
            return;
        }

        final String path = properties.getProperty("tracefile");
        // get the sliding window size in seconds
        final int windowSize = Integer.parseInt(properties.getProperty("window.size", "60"));


        //Get Screen Size
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize(dim.width, dim.height);
        setSize(1024, 768);

        cp = new ContentPanel();

        this.setContentPane(cp);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        setUndecorated(true);


        //Set frame's visibility
        setVisible(true);


        //add the parsers here (to disable a parser comment out
        Parsers.add(new EventParser(dim, windowSize));
        Parsers.add(new MessagesParser(dim, windowSize));
        Parsers.add(new ApplicationParser(dim, windowSize));
        Parsers.add(new ClustersParser(dim, windowSize));


        // Processing panel for visual output.
        final PlotsPanel panel = new PlotsPanel(dim);
        panel.init(Parsers);
        this.add(panel);
        pack();


        // Read sample log file
        try {
            final LogObserver lproc = new LogObserver(path, delay);

            for (int i = 0; i < Parsers.size(); i++) {
                lproc.addObserver(Parsers.get(i));
            }
            final Thread thr = new Thread(lproc);
            thr.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            new PlotsMain(Integer.parseInt(args[0]), args[1]);
        } else if (args.length == 1) {
            new PlotsMain(Integer.parseInt(args[0]), "classes/plots.properties");
        } else {
            log.error("Error while executing please provide:");
            log.error("path , delay , propertyfile");
            new PlotsMain(1, "classes/plots.properties");
        }
    }


    class ContentPanel extends JPanel {

        Image bgimage = null;

        ContentPanel() {
            MediaTracker mt = new MediaTracker(this);

            log.info("BG image set");
            bgimage = Toolkit.getDefaultToolkit().getImage("classes/fet11.jpg");
            mt.addImage(bgimage, 0);
            try {
                mt.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgimage, 0, 0, null);
        }
    }

    class ImagePanel extends JPanel {

        private Image img;

        public ImagePanel(String img) {
            this(new ImageIcon(img).getImage());
        }

        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    }
}