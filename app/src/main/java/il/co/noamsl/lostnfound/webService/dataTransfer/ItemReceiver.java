package il.co.noamsl.lostnfound.webService.dataTransfer;


public interface ItemReceiver<T> {
    void onItemArrived(T item);

    void onRequestFailure();
}
