package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.serverInterface.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;

/**
 * Created by noams on 05/11/2017.
 */

public interface Repository {
    /**
     * Assumed to be synchronized
     * @param itemsReceiver
     * @param requestAgent
     */
    void requestItems(final ItemReceiver itemsReceiver, RequestAgent requestAgent);

    FakeItem getItemById(long itemId);


    void addItem(String text);
}
