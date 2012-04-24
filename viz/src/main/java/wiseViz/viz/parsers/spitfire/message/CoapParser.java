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
 * Time: 2:00 PM
 */
public class CoapParser extends AbstractParser {
    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public CoapParser(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) {
            return;
        }
        SpitfireMessage message = new SpitfireMessage((String) arg);

        if (!message.isValid()) return;
        if ("COAP".equals(message.getApplication())) {


            final VizNode thisNode = displayNode(message.getSrcMac());
            if ("000000000000ffff".equals(message.getDstMac())) {
                thisNode.bcastEvent(Color.blue.getRGB(), message.getPayloadLength(), "");
            } else {
                final VizNode otherNode = displayNode(message.getDstMac());

                VizLink link = displayLink(thisNode, otherNode, VizLink.LINK_BI);
                if (!thisNode.getHexId().equals(link.getSource().getHexId()) 
                        || !otherNode.getHexId().equals(link.getTarget().getHexId())) {
                    System.out.println("!!!!!!!!!!!");
                    System.out.println("Coap message " + thisNode.getHexId()+ "->" + otherNode.getHexId()+ ":" + message.getPayloadLength());
                    System.out.println("Coap message " + link.getSource().getHexId()+ "->" + link.getTarget().getHexId()+ ":" + message.getPayloadLength());
                }
                thisNode.sendPacket(link, Color.blue.getRGB(), message.getPayloadLength(), thisNode, otherNode);
            }
        }
    }
}
