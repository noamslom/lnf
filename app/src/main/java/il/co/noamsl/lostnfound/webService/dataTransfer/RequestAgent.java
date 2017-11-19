package il.co.noamsl.lostnfound.webService.dataTransfer;


public class RequestAgent {
    private volatile int nextRequestSerial = 0;
    private volatile int requestedLeft;
//    private boolean limitedAmount;

    public synchronized int getNextRequestSerial() {
        return nextRequestSerial;
    }

    public synchronized int getRequestedLeft() {
        return requestedLeft;
    }

    public RequestAgent() {
        this.requestedLeft = 0;
//        this.limitedAmount = true;
    }

    public synchronized void next() {
        nextRequestSerial++;
        requestedLeft--;
    }

    public synchronized void addRequested(int addition) {
        requestedLeft +=addition;
    }
}
