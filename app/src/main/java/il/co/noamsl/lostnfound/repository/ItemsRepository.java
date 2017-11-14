package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.serverInterface.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;

/**
 * Created by noams on 13/11/2017.
 */

class ItemsRepository implements ItemReceiver<FakeItem> {

    public void requestItems(ItemReceiver itemsReceiver, RequestAgent requestAgent) {

    }

    @Override
    public void onItemArrived(FakeItem item) {

    }
}
