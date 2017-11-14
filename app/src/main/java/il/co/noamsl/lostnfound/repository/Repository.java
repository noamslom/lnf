package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.item.LFItem;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;

/**
 * Created by noams on 05/11/2017.
 */

public interface Repository {
    /**
     * Assumed to be synchronized
     * @param request
     * @param requestAgent
     */
    void requestItems(final Request<LFItem> request, RequestAgent requestAgent);

    FakeItem getItemById(long itemId);


    void addItem(String text);
}
