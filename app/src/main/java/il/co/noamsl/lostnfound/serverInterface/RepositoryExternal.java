package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.item.FakeItem;

/**
 * Created by noams on 04/08/2017.
 */

public interface RepositoryExternal {

//    long getID();//?

    ItemsBulk getAllItemsItemsBulk();

    ItemsBulk getMyItemsItemsBulk();

    FakeItem getItemById(long itemId);

    void addItem(String text);
}
