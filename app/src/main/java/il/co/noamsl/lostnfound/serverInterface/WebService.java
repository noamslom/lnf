package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.item.LFItem;

/**
 * Created by noams on 05/11/2017.
 */

public interface WebService {
    /**
     * Assumed to be synchronized
     * @param requestAgent
     */
    void requestItems(final Request<LFItem> request, RequestAgent requestAgent);

    FakeItem getItemById(long itemId);


    void addItem(String text);
}
