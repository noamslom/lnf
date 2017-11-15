package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.screens.itemsFeed.itemsBulk.ItemsBulk;

/**
 * Created by noams on 04/08/2017.
 */

public interface RepositoryExternal {

//    long getID();//?

    ItemsBulk getAllItemsItemsBulk();

    ItemsBulk getMyItemsItemsBulk();
    
    void addItem(LfItem lfItem);
}
