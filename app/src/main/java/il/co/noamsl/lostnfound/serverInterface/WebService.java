package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.item.LfItem;

/**
 * Created by noams on 05/11/2017.
 */

public interface WebService {
    /**
     * Assumed to be synchronized
     * @param requestAgent
     */
    void requestItems(final Request<LfItem> request, RequestAgent requestAgent);

    LfItemImpl getItemById(long itemId);


    void addItem(LfItem lfItem);
}
