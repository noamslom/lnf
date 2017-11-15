package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.item.LfItem;
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
    void requestItems(final Request<LfItem> request, RequestAgent requestAgent);


    void addItem(LfItem lfItem);
}
