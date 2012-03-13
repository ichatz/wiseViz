package wiseViz.viz.parsers.spitfire;

import wiseViz.viz.base.VizPanel;
import wiseViz.viz.parsers.AbstractParser;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/22/12
 * Time: 10:00 AM
 */
public class SpitfireTestObserver extends AbstractParser {

    /**
     * Default constructor.
     *
     * @param vPanel the vizualization panel.
     */
    public SpitfireTestObserver(final VizPanel vPanel) {
        super(vPanel);
    }

    public void update(Observable observable, Object o) {
//        if (!(o instanceof String)) {
//            return;
//        }
//
//        SpitfireMessage message = new SpitfireMessage((String) o);
//
//        final VizNode thisNode = displayNode(message.getSrcMac());
//        final VizNode otherNode = displayNode(message.getDstMac());
//
//        VizLink link = displayLink(thisNode, otherNode, VizLink.LINK_BI);
//
//        thisNode.sendPacket(link, Color.blue.getRGB(), 16, thisNode, otherNode);
//
//
//        System.out.println(thisNode.getId());
//        System.out.println(otherNode.getId());
////
////        if (thisNode != null) {
////            Make sure node is not a head
////            thisNode.setClusterHead(false);
////
////            Assign same color with head
////            thisNode.setColorInt(Color.WHITE.getRGB());
////            thisNode.setAggregatedValue("");
////            thisNode.setSensorValue("");
////            thisNode.setEnabled(true);
////        }
//
//        System.out.println(message.getApplication());
////        System.out.println(message.getDstIP());
    }
}
