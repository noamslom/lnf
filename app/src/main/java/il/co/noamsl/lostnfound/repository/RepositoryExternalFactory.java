package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.screens.itemsFeed.itemsBulk.ItemsBulk;

/**
 * Created by noams on 05/11/2017.
 */

public class RepositoryExternalFactory {
    public static RepositoryExternal newInstance(){
        return new RepositoryExternal() {
            @Override
            public ItemsBulk getAllItemsItemsBulk() {
               return getMyItemsItemsBulk();
            }

            @Override
            public ItemsBulk getMyItemsItemsBulk() {
                return new ItemsBulk(Repository.getGlobal(),null);
            }

            @Override
            public void addItem(LfItem lfitem) {
                Repository.getGlobal().addItem(lfitem);
            }
        };
    }

}
