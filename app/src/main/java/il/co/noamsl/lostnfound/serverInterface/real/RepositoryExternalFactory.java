package il.co.noamsl.lostnfound.serverInterface.real;

import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.item.FakeItem;
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
                return new ItemsBulk(ServiceLocator.getInternalServer(),null);
            }

            @Override
            public FakeItem getItemById(long itemId) {
                return ServiceLocator.getInternalServer().getItemById(itemId);
            }

            @Override
            public void addItem(String text) {
                ServiceLocator.getInternalServer().addItem(text);
            }
        };
    }

}
