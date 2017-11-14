package il.co.noamsl.lostnfound.dataTransfer;

/**
 * Created by noams on 05/11/2017.
 */

public interface ItemReceiver<T> {
    void onItemArrived(T item);
}
