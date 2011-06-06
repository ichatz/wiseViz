/*
 * RealTimePlotsView.java
 */
package eu.wisebed.wiseViz.plots;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * The application's main frame.
 */
public class RealTimePlotsView extends FrameView {

    private final JFrame realtime;
    private final ContentPanel cp;
    private final JPanel inpanel;
    private final ChartPanel messages_chpanel;
    private final ChartPanel events_chpanel;
    private final ChartPanel clusters_chpanel;
    private final ChartPanel application_chpanel;
    private TimeSeries messages_series;
    private TimeSeries events_nd_series;
    private TimeSeries events_cl_series;
    private TimeSeries messages_cl_series;
    private TimeSeries messages_clr_series;
    private TimeSeries messages_e2e_series;
    private TimeSeries messages_ags_series;
    private TimeSeries clusters_series;
    private TimeSeries cluster_size_series;
    private TimeSeries application_flsf_series;
    private TimeSeries application_flrf_series;
    private TimeSeries application_flsl_series;
    private TimeSeries application_flrl_series;
    //private final Timer timer;
    JFreeChart messages_chart;
    JFreeChart events_chart;
    JFreeChart clusters_chart;
    JFreeChart application_chart;

    public RealTimePlotsView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);

        messages_cl_series = new TimeSeries("CL Messages", Millisecond.class);
        messages_clr_series = new TimeSeries("CLR Messages", Millisecond.class);
        messages_e2e_series = new TimeSeries("E2E Messages", Millisecond.class);
        messages_ags_series = new TimeSeries("AG Messages", Millisecond.class);
        events_nd_series = new TimeSeries("ND Events", Millisecond.class);
        events_cl_series = new TimeSeries("CL Events", Millisecond.class);
        clusters_series = new TimeSeries("# of Societies", Millisecond.class);
        cluster_size_series = new TimeSeries("Avg size of Societies", Millisecond.class);
        application_flsf_series = new TimeSeries("Fan Send", Millisecond.class);
        application_flrf_series = new TimeSeries("Fan Receive", Millisecond.class);
        application_flsl_series = new TimeSeries("Lamp Send", Millisecond.class);
        application_flrl_series = new TimeSeries("Lamp Receive", Millisecond.class);



        logospanel = new JPanel();
        logospanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        //ImageIcon imageIcon = new ImageIcon(getClass().getResource("/experimentevaluatorq/fronts-logo.jpg"));
        ImagePanel frontsLogo = new ImagePanel(new ImageIcon(getClass().getResource("/eu/wisebed/wiseViz/plots/fronts-logo.png")).getImage());
        logospanel.add(frontsLogo);
        //ImageIcon imageIcon = new ImageIcon(getClass().getResource("/experimentevaluatorq/fronts-logo.jpg"));
        ImagePanel gap = new ImagePanel(new ImageIcon(getClass().getResource("/eu/wisebed/wiseViz/plots/gap.png")).getImage());
        gap.setOpaque(true);
        logospanel.add(gap);
        //ImageIcon imageIcon_fet = new ImageIcon(getClass().getResource("/experimentevaluatorq/fetLogo.png"));
        ImagePanel fetLogo = new ImagePanel(new ImageIcon(getClass().getResource("/eu/wisebed/wiseViz/plots/fetLogo.png")).getImage());
        logospanel.add(fetLogo);
        logospanel.setOpaque(false);
        logospanel.setSize(200, 100);

        realtime = new JFrame();
        cp = new ContentPanel();

        realtime.setContentPane(cp);
        //realtime.setLayout(new GridLayout(2, 2));
        //realtime.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        inpanel = new JPanel();
        inpanel.setLayout(new GridLayout(3, 1));
        inpanel.setOpaque(false);




        realtime.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        realtime.add(inpanel);

        realtime.setUndecorated(true);
        realtime.show();




        JPanel frow = new JPanel();
        JPanel srow = new JPanel();
        frow.setOpaque(false);
        srow.setOpaque(false);
        inpanel.removeAll();
        frow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        srow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JPanel a1 = new JPanel();
        JPanel a2 = new JPanel();
        JPanel a3 = new JPanel();
        JPanel a4 = new JPanel();


        final TimeSeriesCollection messages_dataset = new TimeSeriesCollection();
        messages_dataset.addSeries(messages_cl_series);
        messages_dataset.addSeries(messages_clr_series);
        messages_dataset.addSeries(messages_e2e_series);
        messages_dataset.addSeries(messages_ags_series);
        messages_chart = createChart(messages_dataset, "Messages Sent", "Time", "# of Messages");

        final TimeSeriesCollection events_dataset = new TimeSeriesCollection();
        events_dataset.addSeries(events_nd_series);
        events_dataset.addSeries(events_cl_series);
        events_chart = createChart(events_dataset, "Events", "Time", "# of Events");


        final TimeSeriesCollection clusters_dataset = new TimeSeriesCollection();
        clusters_dataset.addSeries(clusters_series);
        clusters_dataset.addSeries(cluster_size_series);
        clusters_chart = createChart(clusters_dataset, "Clusters", "Time", "# of Nodes");

        final TimeSeriesCollection application_dataset = new TimeSeriesCollection();
        application_dataset.addSeries(application_flrf_series);
        application_dataset.addSeries(application_flsf_series);
        application_dataset.addSeries(application_flrl_series);
        application_dataset.addSeries(application_flsl_series);

        application_chart = createChart(application_dataset, "Application Success Rate", "Time", "");




        messages_chpanel = new ChartPanel(messages_chart);
        events_chpanel = new ChartPanel(events_chart);
        clusters_chpanel = new ChartPanel(clusters_chart);
        application_chpanel = new ChartPanel(application_chart);

        messages_chpanel.setPreferredSize(new Dimension(600, 300));
        events_chpanel.setPreferredSize(new Dimension(600, 300));
        clusters_chpanel.setPreferredSize(new Dimension(600, 300));
        application_chpanel.setPreferredSize(new Dimension(600, 300));

        a1.add(messages_chpanel);
        a2.add(events_chpanel);
        a3.add(clusters_chpanel);
        a4.add(application_chpanel);
        a1.setOpaque(false);
        a2.setOpaque(false);
        a3.setOpaque(false);
        a4.setOpaque(false);
        //a1.setPreferredSize(new Dimension(600,300));
        //      a2.setPreferredSize(new Dimension(600,300));
        //    a3.setPreferredSize(new Dimension(600,300));
        //  a4.setPreferredSize(new Dimension(600,300));

        frow.add(a1);
        frow.add(a2);
        srow.add(a3);
        srow.add(a4);
        //frow.setPreferredSize(new Dimension(400, 200));
        //srow.setPreferredSize(new Dimension(400, 200));

        inpanel.add(frow);
        inpanel.add(srow);



        inpanel.add(logospanel);


        realtime.setVisible(true);
        realtime.setExtendedState(realtime.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        /*messages_chpanel.setSize(realtime.getWidth() * 4 / 10, (int) realtime.getWidth() * 4 / 10);
        chpanel_event.setSize(realtime.getWidth() * 4 / 10, (int) realtime.getWidth() * 4 / 10);
        chpanel_clust.setSize(realtime.getWidth() * 4 / 10, (int) realtime.getWidth() * 4 / 10);
        chpanel_appl.setSize(realtime.getWidth() * 4 / 10, (int) realtime.getWidth() * 4 / 10);*/

        //realtime.setSize(300,300);
        realtime.setVisible(true);

        realtime.pack();

        //start_parsing();
        Parser parser = new Parser(this);
        parser.start();
//        timer = new Timer(1000, new ActionListener() {
//
//        
//            public void actionPerformed(ActionEvent e) {
//                start_parsing();
//                //timer.restart();
//            }
//        });
//        timer.setInitialDelay(2000);
//        timer.start();
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title, String xlabel, String ylabel) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title,
                xlabel,
                ylabel,
                dataset,
                true,
                true,
                false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60 * 1000.0);  // 60 seconds
        //axis = plot.getRangeAxis();
        //axis.setRange(0.0, 200.0); 
        return result;
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = RealTimePlotsApp.getApplication().getMainFrame();
            aboutBox = new RealTimePlotsAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        RealTimePlotsApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(eu.wisebed.wiseViz.plots.RealTimePlotsApp.class).getContext().getResourceMap(RealTimePlotsView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(eu.wisebed.wiseViz.plots.RealTimePlotsApp.class).getContext().getActionMap(RealTimePlotsView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private static JPanel logospanel;

    public class Parser extends Thread {

        public class node {

            public int node_id;
            public boolean head;

            node() {
                node_id = 0;
                head = false;
            }
        };
        private final RealTimePlotsView parent;
        node clusters_node_list[] = new node[40];

        public Parser(RealTimePlotsView frame) {
            parent = frame;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(2000);
                FileInputStream fstream = null;
                try {
                    fstream = new FileInputStream(System.getProperty("user.home") + "/Desktop/trace2.out");

                } catch (Exception ex) {
                    Logger.getLogger(RealTimePlotsView.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                System.out.println("Started Parsing...");

                //events_series.clear();
                int events_nb = 0;
                int events_cl = 0;
                int messages_cl = 0;
                int messages_clr = 0;
                int messages_e2e = 0;
                int messages_ags = 0;
                int application_flsl = 0;
                int application_flrl = 0;
                int application_flsf = 0;
                int application_flrf = 0;
                int clusters_heads = 0;
                int clusters_simple = 0;


                for (int i = 0; i < 40; i++) {
                    clusters_node_list[i] = new node();
                }




                Millisecond m = new Millisecond(new Date());
                events_nd_series.add(m, events_nb);
                events_cl_series.add(m, events_cl);
                messages_cl_series.add(m, messages_cl);
                messages_clr_series.add(m, messages_clr);
                messages_e2e_series.add(m, messages_e2e);
                messages_ags_series.add(m, messages_ags);
                application_flsl_series.add(m, application_flsl);
                application_flrl_series.add(m, application_flrl);
                application_flsf_series.add(m, application_flsf);
                application_flrf_series.add(m, application_flrf);


                String strLine = null;
                while (true) {

                    try {
                        strLine = br.readLine();
                    } catch (Exception ex) {
                        Logger.getLogger(RealTimePlotsView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    final Millisecond now = new Millisecond(new Date());
                    Thread.sleep(1);
                    // Print the content on the console
                    if (strLine != null) {
                        try {
                            if (strLine.contains("NB")) {
                                //System.out.println("adding--" + now.getMillisecond());
                                events_nd_series.add(now, ++events_nb);
                                events_cl_series.add(now, events_cl);
                            } else if (strLine.contains("CLP")) {
                                events_cl_series.add(now, ++events_cl);
                                events_nd_series.add(now, events_nb);
                            } else if (strLine.contains("CLS")) {
                                messages_cl_series.add(now, ++messages_cl);
                                messages_clr_series.add(now, messages_clr);
                                messages_e2e_series.add(now, messages_e2e);
                                messages_ags_series.add(now, messages_ags);
                            } else if (strLine.contains("CLRS")) {
                                messages_cl_series.add(now, messages_cl);
                                messages_clr_series.add(now, ++messages_clr);
                                messages_e2e_series.add(now, messages_e2e);
                                messages_ags_series.add(now, messages_ags);
                            } else if (strLine.contains("E2E")) {
                                messages_cl_series.add(now, messages_cl);
                                messages_clr_series.add(now, messages_clr);
                                messages_e2e_series.add(now, ++messages_e2e);
                                messages_ags_series.add(now, messages_ags);
                            } else if (strLine.contains("AGS")) {
                                messages_cl_series.add(now, messages_cl);
                                messages_clr_series.add(now, messages_clr);
                                messages_e2e_series.add(now, messages_e2e);
                                messages_ags_series.add(now, ++messages_ags);
                            } else if (strLine.contains("FLS:lamp")) {
                                application_flsl_series.add(now, ++application_flsl);
                                application_flrl_series.add(now, application_flrl);
                                application_flsf_series.add(now, application_flsf);
                                application_flrf_series.add(now, application_flrf);
                            } else if (strLine.contains("FLR:lamp")) {
                                application_flsl_series.add(now, application_flsl);
                                application_flrl_series.add(now, ++application_flrl);
                                application_flsf_series.add(now, application_flsf);
                                application_flrf_series.add(now, application_flrf);
                            } else if (strLine.contains("FLS:fan")) {
                                application_flsl_series.add(now, application_flsl);
                                application_flrl_series.add(now, application_flrl);
                                application_flsf_series.add(now, ++application_flsf);
                                application_flrf_series.add(now, application_flrf);
                            } else if (strLine.contains("FLR:fan")) {
                                application_flsl_series.add(now, application_flsl);
                                application_flrl_series.add(now, application_flrl);
                                application_flsf_series.add(now, application_flsf);
                                application_flrf_series.add(now, ++application_flrf);
                            }

                            if (strLine.contains("CLP")) {
                                int clp_start = strLine.indexOf("CLP") + 3;
                                int clp_end = strLine.lastIndexOf(";");
                                String clpevent = strLine.substring(clp_start, clp_end);


                                String[] a = clpevent.split(";");
                                int type = Integer.parseInt(a[2]);
                                int nodeid = Integer.parseInt(a[1].substring(2), 16);



                                if (type == 2) {
                                    set_head(nodeid);
                                } else if (type != 0) {
                                    set_simple(nodeid);
                                }

                                if (heads() > 0) {
                                    clusters_series.add(now, heads());
                                    cluster_size_series.add(now, total() / heads());
                                    //System.out.println("Found clp," + nodeid + "," + type + " Addin:" + heads() + " div: " + total() / heads());
                                } else {
                                    clusters_series.add(now, 0);
                                    cluster_size_series.add(now, 0);
                                }

                            }

                        } catch (org.jfree.data.general.SeriesException e) {
                            System.out.println("Series Exc:" + e.toString());
                        }
                    } else {
                        events_nd_series.add(now, events_nb);
                        events_cl_series.add(now, events_nb);
                        messages_cl_series.add(now, messages_cl);
                        messages_clr_series.add(now, messages_clr);
                        messages_e2e_series.add(now, messages_e2e);
                        messages_ags_series.add(now, messages_ags);
                        application_flsl_series.add(now, application_flsl);
                        application_flrl_series.add(now, application_flrl);
                        application_flsf_series.add(now, application_flsf);
                        application_flrf_series.add(now, application_flrf);
                        if ((heads() + simples()) > 0) {
                            clusters_series.add(now, heads());
                            cluster_size_series.add(now, total() / heads());
                        } else {
                            clusters_series.add(now, 0);
                            cluster_size_series.add(now, 0);
                        }
                    }

                    //                Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(RealTimePlotsView.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

        private int find(int id) {
            int mark = -1;
            for (int i = 0; i < 40; i++) {
                if (clusters_node_list[i].node_id == id) {
                    return i;
                } else if (clusters_node_list[i].node_id == 0) {
                    mark = i;
                }
            }
            if (mark != -1) {
                clusters_node_list[mark].node_id = id;
                clusters_node_list[mark].head = false;
                return mark;
            }
            return -1;
        }

        private void set_head(int id) {
            int mark = find(id);
            //System.out.println("Found in pos "+mark);
            if (mark != -1) {

                clusters_node_list[mark].head = true;
            }
        }

        private void set_simple(int id) {
            int mark = find(id);
            if (mark != -1) {

                clusters_node_list[mark].head = false;

            }
        }

        private double heads() {
            double heads = 0;
            for (int i = 0; i < 40; i++) {
                if (clusters_node_list[i].head) {
                    heads++;
                }
            }
            return heads;
        }

        private double simples() {
            double simples = 0;
            for (int i = 0; i < 40; i++) {
                if (clusters_node_list[i].head == false) {
                    simples++;
                }
            }
            return simples;
        }

        private double total() {
            double total = 0;
            for (int i = 0; i < 40; i++) {
                if (clusters_node_list[i].node_id > 0) {
                    total++;
                }
            }
            return total;
        }
    }

    class ContentPanel extends JPanel {

        Image bgimage = null;

        ContentPanel() {
            MediaTracker mt = new MediaTracker(this);

            bgimage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/eu/wisebed/wiseViz/plots/fet11.jpg"));
            //bgimage = Toolkit.getDefaultToolkit().getImage("/experimentevaluatorq/fet11.jpg");
            mt.addImage(bgimage, 0);
            try {
                mt.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int imwidth = bgimage.getWidth(null);
            int imheight = bgimage.getHeight(null);
            g.drawImage(bgimage, 1, 1, null);
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
