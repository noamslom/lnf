package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.item.Item;
import il.co.noamsl.lostnfound.item.RequestAgent;

/**
 * Created by noams on 05/11/2017.
 */

public interface NoamServerInternal {
    /**
     * Assumed to be synchronized
     * @param itemsReceiver
     * @param requestAgent
     */
    void requestItems(final ItemReceiver itemsReceiver, RequestAgent requestAgent);

    Item getItemById(long itemId);


    void addItem(String text);
}
