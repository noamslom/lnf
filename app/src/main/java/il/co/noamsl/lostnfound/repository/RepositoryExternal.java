package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.screens.itemsFeed.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

/**
 * Created by noams on 04/08/2017.
 */

public class RepositoryExternal {

    public ItemsBulk getAllItemsItemsBulk() {
        return getMyItemsItemsBulk();
    }

    public ItemsBulk getMyItemsItemsBulk() {
        return new ItemsBulk(Repository.getGlobal(),null);
    }

    public void addItem(LfItem lfitem) {
        Repository.getGlobal().addItem(lfitem);
    }

    public LfItem getItemById(int itemId) {
        return Repository.getGlobal().getItemById(itemId);
    }

    public void getUserById(ItemReceiver<User> itemReceiver, int owner) {
        Repository.getGlobal().getUserById(itemReceiver,owner);
    }
}
