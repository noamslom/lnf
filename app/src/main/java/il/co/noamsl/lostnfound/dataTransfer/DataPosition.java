package il.co.noamsl.lostnfound.dataTransfer;

/**
 * Created by noams on 14/11/2017.
 */

public class DataPosition<T> {
    private T last;

    public T getLast() {
        return last;
    }

    public DataPosition(T last) {

        this.last = last;
    }
}
