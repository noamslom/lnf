package il.co.noamsl.lostnfound.repository.external;

import il.co.noamsl.lostnfound.repository.Repository;
import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.external.itemsBulk.MyItemsItemsBulk;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.repository.external.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

/**
 * Created by noams on 04/08/2017.
 */

public class RepositoryExternal {

    public ItemsBulk getAllItemsItemsBulk() {
        return new ItemsBulk(Repository.getGlobal(),null);
    }

    public ItemsBulk getMyItemsItemsBulk() {
        return new MyItemsItemsBulk(Repository.getGlobal());
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

    public void updateItem(LfItem newItem) {
        Repository.getGlobal().updateItem(newItem);
    }

    public void updateUser(User user) {
        Repository.getGlobal().updateUser(user);
    }

    public User getLoggedInUser() {
        return Repository.getGlobal().getLoggedInUser();
    }
}
