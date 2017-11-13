package il.co.noamsl.lostnfound.item;

/**
 * Created by noams on 05/11/2017.
 */

public class RequestAgent {
    private volatile int nextRequestSerial = 0;
    private volatile int requestedLeft;


    public int getNextRequestSerial() {
        return nextRequestSerial;
    }

    public int getRequestedLeft() {
        return requestedLeft;
    }

    public RequestAgent() {
        this.requestedLeft = 0;
    }

    public synchronized void next() {
        nextRequestSerial++;
        requestedLeft--;
    }

    public void addRequested(int addition) {
        requestedLeft +=addition;
    }
}
