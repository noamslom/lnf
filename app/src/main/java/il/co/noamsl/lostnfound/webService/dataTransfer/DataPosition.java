package il.co.noamsl.lostnfound.webService.dataTransfer;


public class DataPosition<T> {
    private T last;

    public DataPosition(T last) {

        this.last = last;
    }

    public T getLast() {
        return last;
    }

    @Override
    public String toString() {
        return "DataPosition{" +
                "last=" + last +
                '}';
    }
}
