package il.co.noamsl.lostnfound.serverInterface.fake;

import il.co.noamsl.lostnfound.item.Item;
import il.co.noamsl.lostnfound.item.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;
import il.co.noamsl.lostnfound.serverInterface.NoamServerInternal;

/**
 * Created by noams on 13/11/2017.
 */

public class ServerInternal implements NoamServerInternal {


    @Override
    public void requestItems(ItemReceiver itemsReceiver, RequestAgent requestAgent) {

    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public void addItem(String text) {

    }
}
