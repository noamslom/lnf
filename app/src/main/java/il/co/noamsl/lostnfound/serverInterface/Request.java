package il.co.noamsl.lostnfound.serverInterface;

import junit.framework.Assert;

import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;

/**
 * Created by noams on 05/11/2017.
 */

public class Request<T> {
    private final ItemReceiver<T> ITEM_RECEIVER;
    private final Integer MAX_ITEMS;


    public Request(ItemReceiver<T> itemReceiver) {
        this(itemReceiver, null);
    }

    public ItemReceiver<T> getItemReceiver() {
        return ITEM_RECEIVER;
    }

    public Integer getMaxItems() {
        return MAX_ITEMS;
    }

    /**
     *
     * @param itemReceiver
     * @param maxItems - number of items to request. unlimited if null
     */
    public Request(ItemReceiver<T> itemReceiver, Integer maxItems) {
        Assert.assertEquals(maxItems,null);
        this.ITEM_RECEIVER = itemReceiver;
        this.MAX_ITEMS = maxItems;
    }
}
