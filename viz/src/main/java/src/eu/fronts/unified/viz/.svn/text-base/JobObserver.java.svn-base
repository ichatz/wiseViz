package eu.fronts.unified.viz;

import eu.wisebed.testbed.api.wsn.v211.Controller;
import eu.wisebed.testbed.api.wsn.v211.Message;
import eu.wisebed.testbed.api.wsn.v211.RequestStatus;

import java.util.Observable;

/**
 * Observes to the traces directly from the testbed.
 */
public class JobObserver extends Observable implements Controller, Runnable {

    public JobObserver() {

    }

    public void receive(Message msg) {
        // Message received
        System.out.println(msg);
    }

    public void start() {

    }

    public void end() {

    }

    public void receiveStatus(RequestStatus status) {
        // ignore
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
        // do nothing
    }

}
