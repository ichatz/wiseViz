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
    private static String SEPARATOR = "\\]";
    private static int TIME = 0;
    private static int PROTOCOL = 1;
    private static int DESTINATION = 2;
    private static int SRC_MAC = 3;
    private static int DST_MAC = 4;
    private static int SRC_IP = 5;
    private static int DST_IP = 6;
    private static int TRANSPORT = 7;
    private static int APPLICATION = 8;
    private static int PAYLOAD = 9;

    private String[] message;

    private final boolean valid;

    public SpitfireMessage(String text) {
        if (text.startsWith("[") && text.split(SEPARATOR).length >= 10) {
            valid = true;
            this.message = text.split(SEPARATOR);
        } else {
            valid = false;
        }

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
        System.out.println(message.length);
        return deframeText(message[APPLICATION]);
    }

    public String getPayload() {
        if (getApplication().equals("COAP")) {
            return deframeText(message[PAYLOAD + 1]);
        } else {
            return deframeText(message[PAYLOAD]);
        }
    }

    public boolean isValid() {
        return valid;
    }

    public int getPayloadLength() {
        if (getApplication().equals("COAP")) {
            int size = deframeText(message[PAYLOAD + 1]).length();
            if (size > 500 / 8) {
                size = 500 / 6;
            }
            if (size == 0) {
                size = 5;

            }
            return size;
        } else {
            return deframeText(message[PAYLOAD]).length();
        }
//        return (deframeText(message[PAYLOAD]).length() < 10) ? deframeText(message[PAYLOAD]).length() : 10;
    }

    private String deframeText(final String string) {
        return string.substring(1);
    }
}
