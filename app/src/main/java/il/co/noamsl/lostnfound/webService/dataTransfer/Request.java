package il.co.noamsl.lostnfound.webService.dataTransfer;

import junit.framework.Assert;

/**
 * Created by noams on 05/11/2017.
 */

public class Request<T> {
    private final ItemReceiver<T> ITEM_RECEIVER;
    private final Integer MAX_ITEMS;
    private final DataPosition<T> dataPosition;

    public Request(ItemReceiver<T> itemReceiver,DataPosition<T> dataPosition) {
        this(itemReceiver, null,dataPosition);
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
    public Request(ItemReceiver<T> itemReceiver, Integer maxItems,DataPosition<T> dataPosition) {
        Assert.assertEquals(maxItems,null);
        this.ITEM_RECEIVER = itemReceiver;
        this.MAX_ITEMS = maxItems;
        this.dataPosition = dataPosition;
    }

    public DataPosition<T> getDataPosition() {
        return dataPosition;
    }
}
