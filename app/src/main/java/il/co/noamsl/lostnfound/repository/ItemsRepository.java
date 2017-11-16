package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.repository.cache.LFItemsCache;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.WebService;

/**
 * Created by noams on 13/11/2017.
 */

class ItemsRepository {
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
                if (item != null) {
                    itemsCache.add(item);
                    request.getItemReceiver().onItemArrived(itemsCache.getItem(item.getId()+""));
                }
                else {
                    request.getItemReceiver().onItemArrived(null);
                }
            }
        }  ;
        webService.requestItems(new Request<LfItem>(itemReceiver,request.getDataPosition()),null,new ItemsQuery("wal",null,null,false));

    }

}
