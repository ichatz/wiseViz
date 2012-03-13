package wiseViz.viz.parsers.spitfire.message;

import wiseViz.viz.base.VizLink;
import wiseViz.viz.base.VizNode;
import wiseViz.viz.base.VizPanel;
import wiseViz.viz.message.SpitfireMessage;
import wiseViz.viz.parsers.AbstractParser;

import java.awt.*;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/23/12
 * Time: 2:02 PM
 */
public class RaRsParser extends AbstractParser {
    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public RaRsParser(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) {
            return;
        }

        SpitfireMessage message = new SpitfireMessage((String) arg);

        if ("RA".equals(message.getApplication()) || "RS".equals(message.getApplication())) {

            final VizNode thisNode = displayNode(message.getSrcMac());
            if ("000000000000ffff".equals(message.getDstMac())) {
                thisNode.bcastEvent(Color.green.getRGB(), message.getPayload().length() * 10, "");
            } else {
                final VizNode otherNode = displayNode(message.getDstMac());

                VizLink link = displayLink(thisNode, otherNode, VizLink.LINK_BI);


                thisNode.sendPacket(link, Color.green.getRGB(), message.getPayload().length(), thisNode, otherNode);
            }
        }
    }
}
