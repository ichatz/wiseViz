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
public class TRMessage implements Message {
    private static String SEPARATOR = "]";
    private static int SRC_URN = 0;
    private static int PAYLOAD = 1;
    private static int LEVEL = 2;
    private static int TIME = 3;

    private String[] message;
    private final boolean valid;


    public TRMessage(String text) {
        if (text.startsWith("Source [") && text.split(SEPARATOR).length == 4) {
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
        return deframeText(message[LEVEL]);
    }

    public boolean getDestination() {
        return false;
    }

    public String getSrcMac() {
        return deframeText(message[SRC_URN]);
    }

    public String getDstMac() {
        return "";
    }

    public String getSrcIP() {
        return "";
    }

    public String getDstIP() {
        return "";
    }

    public String getTransport() {
        return "";
    }

    public String getApplication() {
        return "";
    }

    public String getPayload() {
        return deframeText(message[PAYLOAD]);
    }

    public boolean isValid() {
        return valid;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPayloadLength() {
        return deframeText(message[PAYLOAD]).length();
    }

    private String deframeText(final String string) {
//        System.out.println("STring "+string.indexOf("[")+" is "+string);
        return string.substring(string.indexOf("["));
    }
}
