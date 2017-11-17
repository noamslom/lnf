package il.co.noamsl.lostnfound.webService.dataTransfer;

/**
 * Created by noams on 05/11/2017.
 */

public interface ItemReceiver<T> {
    void onItemArrived(T item);

    void onRequestFailure();
}
