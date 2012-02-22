package wiseViz.viz.message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/21/12
 * Time: 6:48 PM
 */
public class SpitfireMessage implements Message {
    private static String SEPARATOR = "\\[";
    private static int TIME = 1;
    private static int PROTOCOL = 2;
    private static int DESTINATION = 3;
    private static int SRC_MAC = 4;
    private static int DST_MAC = 5;
    private static int SRC_IP = 6;
    private static int DST_IP = 7;
    private static int TRANSPORT = 8;
    private static int APPLICATION = 9;
    private static int PAYLOAD = 10;

    private String[] message;

    public SpitfireMessage(String text) {
        this.message = text.split(SEPARATOR);
        System.out.println("text is :" + text);
        System.out.println("size is " + message.length);
    }

    public Date getTimestamp() {
        final String tempStr = deframeText(message[TIME]);

        final DateFormat dateFormat = new SimpleDateFormat("H:m:s.S");
        Date time = null;
        try {
            time = dateFormat.parse(tempStr);
        } catch (ParseException e) {
        }
        return time;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getProtocol() {
        return deframeText(message[PROTOCOL]);
    }

    public boolean getDestination() {
        final String tempStr = deframeText(message[DESTINATION]);

        if ("IN".equals(tempStr)) {
            return true;
        }
        return false;
    }

    public String getSrcMac() {
        return deframeText(message[SRC_MAC]);
    }

    public String getDstMac() {
        return deframeText(message[DST_MAC]);
    }

    public String getSrcIP() {
        return deframeText(message[SRC_IP]);
    }

    public String getDstIP() {
        return deframeText(message[DST_IP]);
    }

    public String getTransport() {
        return deframeText(message[TRANSPORT]);
    }

    public String getApplication() {
        return deframeText(message[APPLICATION]);
    }

    public String getPayload() {
        return deframeText(message[PAYLOAD]);
    }

    public String getNodeId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getText() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String deframeText(final String string) {

        return string.substring(0, string.length() - 1);
    }
}
