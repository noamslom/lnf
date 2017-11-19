package il.co.noamsl.lostnfound.webService.dataTransfer;

import junit.framework.Assert;


public class Request<T> {
    private final ItemReceiver<T> ITEM_RECEIVER;
    private final Integer MAX_ITEMS;
    private final DataPosition<T> dataPosition;
    private final Query query;

    public Request(ItemReceiver<T> itemReceiver, DataPosition<T> dataPosition, Query query) {
        this(itemReceiver, null, dataPosition, query);
    }

    /**
     * @param itemReceiver
     * @param maxItems     - number of items to request. unlimited if null
     */
    public Request(ItemReceiver<T> itemReceiver, Integer maxItems, DataPosition<T> dataPosition, Query query) {
        Assert.assertEquals(maxItems, null);
        this.ITEM_RECEIVER = itemReceiver;
        this.MAX_ITEMS = maxItems;
        this.dataPosition = dataPosition;
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public ItemReceiver<T> getItemReceiver() {
        return ITEM_RECEIVER;
    }

    public Integer getMaxItems() {
        return MAX_ITEMS;
    }

    public DataPosition<T> getDataPosition() {
        return dataPosition;
    }

    @Override
    public String toString() {
        return "Request{" +
                "ITEM_RECEIVER=" + ITEM_RECEIVER +
                ", MAX_ITEMS=" + MAX_ITEMS +
                ", dataPosition=" + dataPosition +
                ", query=" + query +
                '}';
    }
}
