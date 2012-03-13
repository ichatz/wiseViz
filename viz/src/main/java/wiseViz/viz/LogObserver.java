package wiseViz.viz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Observable;

/**
 * Examines a log file and produces events for the Vizualizer.
 */
public class LogObserver extends Observable implements Runnable {

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
        final FileReader fread = new FileReader(path);
        bread = new BufferedReader(fread);
        delay = theDelay;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
            Thread.sleep(3000);
            StringBuilder line = new StringBuilder();
            while (true) {
                final String rline = bread.readLine();
                if ((rline != null) && (rline.length() > 0)) {
                    line.append(rline);

                    if (line.toString().endsWith("]")) {
//                        System.out.println("line ends normally");
//                        System.out.println("--||" + line.toString() + "||--");
                        setChanged();
                        try {
                            notifyObservers(line.toString());
                        } catch (Exception ex) {
                            System.out.println("--||" + line.toString() + "||--");
                            ex.printStackTrace();
                            System.out.println("---------");
                        }
                        line = new StringBuilder();
                    } else {
//                        System.out.println("line ends with new line");
                    }


                }
                Thread.sleep(delay);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
