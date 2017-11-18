package il.co.noamsl.lostnfound.repository.external;

import il.co.noamsl.lostnfound.ServiceLocator;
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
        return new ItemsBulk(ServiceLocator.getRepository(),null);
    }

    public ItemsBulk getMyItemsItemsBulk() {
        return new MyItemsItemsBulk(ServiceLocator.getRepository());
    }

    public void addItem(final ItemReceiver<Boolean> itemReceiver,LfItem lfitem) {
        ServiceLocator.getRepository().addItem(itemReceiver,lfitem);
    }

    public LfItem getItemById(int itemId) {
        return ServiceLocator.getRepository().getItemById(itemId);
    }

    public void getUserById(ItemReceiver<User> itemReceiver, int owner) {
        ServiceLocator.getRepository().getUserById(itemReceiver,owner);
    }

    public void updateItem(final ItemReceiver<Boolean> itemReceiver,LfItem newItem) {
        ServiceLocator.getRepository().updateItem(itemReceiver,newItem);
    }

    public void updateUser(User user) {
        ServiceLocator.getRepository().updateUser(user);
    }

    public User getLoggedInUser() {
        return ServiceLocator.getRepository().getLoggedInUser();
    }
}
