package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.item.Item;

/**
 * Created by noams on 04/08/2017.
 */

public interface NoamServerExternal {

    long getID();//?

    ItemsBulk getAllItemsItemsBulk();

    ItemsBulk getMyItemsItemsBulk();

    Item getItemById(long itemId);

    void addItem(String text);
}
