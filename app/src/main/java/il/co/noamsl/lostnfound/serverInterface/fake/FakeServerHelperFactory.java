package il.co.noamsl.lostnfound.serverInterface.fake;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.serverInterface.ItemsBulk;
import il.co.noamsl.lostnfound.serverInterface.NoamRepositoryExternal;

/**
 * Created by noams on 05/11/2017.
 */

public class FakeServerHelperFactory {
    public static NoamRepositoryExternal newInstance(){
        //TODO implement this
        return new NoamRepositoryExternal() {
            @Override
            public ItemsBulk getAllItemsItemsBulk() {
               return getMyItemsItemsBulk();
            }

            @Override
            public ItemsBulk getMyItemsItemsBulk() {
                return new ItemsBulk(FakeServerInternal.getGlobalFakeServer(),null);
            }

            @Override
            public FakeItem getItemById(long itemId) {
                return FakeServerInternal.getGlobalFakeServer().getItemById(itemId);
            }

            @Override
            public void addItem(String text) {
                FakeServerInternal.getGlobalFakeServer().addItem(text);
            }
        };
    }

}
