package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.dataTransfer.ItemQuery;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.item.LfItem;

/**
 * Created by noams on 05/11/2017.
 */

public interface WebService {


    void requestItems(Request<LfItem> request, RequestAgent requestAgent, ItemQuery query);

    void addItem(LfItem lfItem);

    void updateItem(LfItem lfItem);
}
