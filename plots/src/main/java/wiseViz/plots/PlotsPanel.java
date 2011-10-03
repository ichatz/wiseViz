package wiseViz.plots;

import org.apache.log4j.Logger;
import wiseViz.plots.parsers.AbstractParser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/30/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlotsPanel extends JPanel {
    private static Logger log = Logger.getLogger(PlotsMain.class);

    private JPanel plotspanel;
    private JPanel logospanel;

    int height;
    int width;

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public PlotsPanel(Dimension dim) {

        height = (int) (dim.height * 0.3);
        width = (int) (dim.width * 0.45);
    }

    public void init(List<AbstractParser> parsers) {

        this.setLayout(new GridLayout(3, 1));

        JPanel frow = new JPanel();
        JPanel srow = new JPanel();
        frow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        srow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        if (parsers.size() == 0) {

        } else if (parsers.size() < 3) {

            for (int i = 0; i < parsers.size(); i++) {
                frow.add(parsers.get(i).getChart());
            }
        } else if (parsers.size() < 5) {

            for (int i = 0; i < 2; i++) {
                frow.add(parsers.get(i).getChart());
            }
            for (int i = 2; i < parsers.size(); i++) {
                srow.add(parsers.get(i).getChart());
            }
        }

        frow.setOpaque(false);
        srow.setOpaque(false);

        this.add(frow);
        this.add(srow);
        this.add(new LogosPanel());
        this.setOpaque(false);
    }
}
