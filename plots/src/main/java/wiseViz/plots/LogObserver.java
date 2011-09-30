package wiseViz.plots;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Observable;

import static java.lang.System.exit;


/**
 * Examines a log file and produces events for the Vizualizer.
 */
public class LogObserver extends Observable implements Runnable {
    private static Logger log = Logger.getLogger(PlotsMain.class);


    /**
     * Buffered reader for accessing the trace file.
     */
    private final BufferedReader bread;

    /**
     * the delay in milliseconds after processing a trace line.
     */
    private int delay;

    /**
     * Default constructor.
     *
     * @param path     the filename to parse.
     * @param theDelay the delay in milliseconds after processing a trace line.
     * @throws Exception in case an error occurred while trying to open the trace file.
     */
    public LogObserver(final String path, final int theDelay) throws Exception {
        super();
        FileReader fread = null;
        try {
            fread = new FileReader(path);

        } catch (Exception e) {
            e.printStackTrace();

            exit(0);
        }
        bread = new BufferedReader(fread);
        delay = theDelay;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
            Thread.sleep(3000);
            while (true) {
                final String line = bread.readLine();
                if ((line != null) && (line.length() > 0)) {
                    setChanged();
                    try {

                        notifyObservers(line);
//                        log.info("...");
                    } catch (Exception ex) {
                        log.info("--||" + line + "||--");
                        ex.printStackTrace();
                        log.info("---------");
                    }

                }
                Thread.sleep(delay);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
