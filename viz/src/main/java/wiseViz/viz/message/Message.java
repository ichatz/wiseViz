package wiseViz.viz.message;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/21/12
 * Time: 6:49 PM
 */
public interface Message {

    public Date getTimestamp();

    public String getNodeId();

    public String getText();

    public String getProtocol();

    public boolean getDestination();

    public String getSrcMac();

    public String getDstMac();

    public String getSrcIP();

    public String getDstIP();

    public String getTransport();

    public String getApplication();

    public String getPayload();

}
