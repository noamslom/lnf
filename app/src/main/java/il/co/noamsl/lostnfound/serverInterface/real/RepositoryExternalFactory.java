package il.co.noamsl.lostnfound.serverInterface.real;

import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.repository.RepositoryImpl;
import il.co.noamsl.lostnfound.serverInterface.ItemsBulk;
import il.co.noamsl.lostnfound.serverInterface.NoamRepositoryExternal;

/**
 * Created by noams on 05/11/2017.
 */

public class RepositoryExternalFactory {
    public static NoamRepositoryExternal newInstance(){
        return new NoamRepositoryExternal() {
            @Override
            public ItemsBulk getAllItemsItemsBulk() {
               return getMyItemsItemsBulk();
            }

            @Override
            public ItemsBulk getMyItemsItemsBulk() {
                return new ItemsBulk(RepositoryImpl.getGlobal(),null);
            }

            @Override
            public FakeItem getItemById(long itemId) {
                return RepositoryImpl.getGlobal().getItemById(itemId);
            }

            @Override
            public void addItem(String text) {
                RepositoryImpl.getGlobal().addItem(text);
            }
        };
    }

}
