package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.item.LFItem;
import il.co.noamsl.lostnfound.repository.cache.LFItemsCache;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.serverInterface.WebService;

/**
 * Created by noams on 13/11/2017.
 */

class ItemsRepository implements ItemReceiver<FakeItem> {
    private LFItemsCache itemsCache;
    private WebService webService;

    public ItemsRepository(WebService webService) {
        this.webService = webService;
        this.itemsCache = new LFItemsCache();
    }

    public void requestItems(final Request<LFItem> request, RequestAgent requestAgent) {
        ItemReceiver<LFItem> itemReceiver = new ItemReceiver<LFItem>() {
            @Override
            public void onItemArrived(LFItem item) {
                itemsCache.add(item);
                request.getItemReceiver().onItemArrived(itemsCache.getItem(item.getId()));
            }
        }  ;
        webService.requestItems(new Request<LFItem>(itemReceiver,request.getDataPosition()),null);

    }

    @Override
    public void onItemArrived(FakeItem item) {

    }
}
