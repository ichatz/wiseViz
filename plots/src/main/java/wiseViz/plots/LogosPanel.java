package wiseViz.plots;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogosPanel extends JPanel {
    private static Logger log = Logger.getLogger(PlotsMain.class);


    public LogosPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel frontsLogo = new JLabel(new ImageIcon("classes/fronts-logo.png", ""));
        log.info("done flogo");
        frontsLogo.setOpaque(false);
        add(frontsLogo);

        JLabel gap = new JLabel(new ImageIcon("classes/gap.png", ""));
        log.info("done gap");
        gap.setOpaque(false);
        add(gap);

        JLabel fetLogo = new JLabel(new ImageIcon("classes/fetLogo.png", ""));
        log.info("done fetlogo");
        fetLogo.setOpaque(false);
        add(fetLogo);

        setSize(200, 100);
        setOpaque(false);
    }

}
