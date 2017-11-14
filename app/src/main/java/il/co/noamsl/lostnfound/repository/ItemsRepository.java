package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.item.LfItem;
import il.co.noamsl.lostnfound.repository.cache.LFItemsCache;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.serverInterface.WebService;

/**
 * Created by noams on 13/11/2017.
 */

class ItemsRepository implements ItemReceiver<LfItemImpl> {
    private LFItemsCache itemsCache;
    private WebService webService;

    public ItemsRepository(WebService webService) {
        this.webService = webService;
        this.itemsCache = new LFItemsCache();
    }

    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent) {
        ItemReceiver<LfItem> itemReceiver = new ItemReceiver<LfItem>() {
            @Override
            public void onItemArrived(LfItem item) {
                itemsCache.add(item);
                request.getItemReceiver().onItemArrived(itemsCache.getItem(item.getId()));
            }
        }  ;
        webService.requestItems(new Request<LfItem>(itemReceiver,request.getDataPosition()),null);

    }

    @Override
    public void onItemArrived(LfItemImpl item) {

    }
}
