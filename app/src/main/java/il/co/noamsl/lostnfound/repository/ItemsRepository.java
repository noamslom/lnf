package il.co.noamsl.lostnfound.repository;


import java.util.List;

import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.WebService;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;

class ItemsRepository {
    private static final String TAG = "ItemsRepository";
    private LFItemsCache itemsCache;
    private WebService webService;

    public ItemsRepository(WebService webService) {
        this.webService = webService;
        this.itemsCache = new LFItemsCache();
    }

    @Override
    public String toString() {
        return "ItemsRepository{" +
                "itemsCache=" + itemsCache +
                '}';
    }

    public void requestItems(final Request<LfItem> request, RequestAgent requestAgent) {
        List<LfItem> cachedItems = itemsCache.get((ItemsQuery) request.getQuery());

        for (LfItem cachedItem : cachedItems) {
            request.getItemReceiver().onItemArrived(cachedItem);
        }

        final ItemReceiver<LfItem> itemReceiver = new ItemReceiver<LfItem>() {
            @Override
            public void onItemArrived(LfItem item) {
                if (item != null) {
                    itemsCache.add(item);
                    request.getItemReceiver().onItemArrived(itemsCache.get(item.getId() + ""));
                } else {
                    request.getItemReceiver().onItemArrived(null);
                }
            }

            @Override
            public void onRequestFailure() {
                request.getItemReceiver().onRequestFailure();
            }
        };
        webService.requestItems(new Request<LfItem>(itemReceiver, request.getDataPosition(), request.getQuery()), null);

    }

    public LfItem getItemById(int id) {
        return itemsCache.get(id + "");
    }

    public void updateItem(final ItemReceiver<Boolean> itemReceiver, final LfItem newItem) {
        webService.updateItem(new ItemReceiver<Boolean>() {
            @Override
            public void onItemArrived(Boolean success) {
                if (!success)
                    throw new IllegalStateException();
                itemsCache.updateItem(newItem);
                itemReceiver.onItemArrived(success);
            }

            @Override
            public void onRequestFailure() {
                itemReceiver.onRequestFailure();
            }
        }, newItem);
    }

    public void addItem(final ItemReceiver<Boolean> itemReceiver, final LfItem lfItem) {
        webService.addItem(new ItemReceiver<Integer>() {
            @Override
            public void onItemArrived(Integer id) {
                if (id < 0)
                    throw new IllegalStateException();
                lfItem.setRecordid(id);
                itemsCache.add(lfItem); //careful
                itemReceiver.onItemArrived(id >= 0);
            }

            @Override
            public void onRequestFailure() {
                itemReceiver.onRequestFailure();

            }
        }, lfItem);

    }

    public void clear() {
        itemsCache.clear();
    }
}
