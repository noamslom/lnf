package il.co.noamsl.lostnfound.serverInterface.fake;

import il.co.noamsl.lostnfound.item.Item;
import il.co.noamsl.lostnfound.serverInterface.ItemsBulk;
import il.co.noamsl.lostnfound.serverInterface.NoamServerExternal;

/**
 * Created by noams on 05/11/2017.
 */

public class FakeServerHelperFactory {
    public static NoamServerExternal newInstance(){
        //TODO implement this
        return new NoamServerExternal() {
            @Override
            public long getID() {
                return 0;
            }

            @Override
            public ItemsBulk getAllItemsItemsBulk() {
               return getMyItemsItemsBulk();
            }

            @Override
            public ItemsBulk getMyItemsItemsBulk() {
                return new ItemsBulk(FakeServerInternal.getGlobalFakeServer(),null);
            }

            @Override
            public Item getItemById(long itemId) {
                return FakeServerInternal.getGlobalFakeServer().getItemById(itemId);
            }

            @Override
            public void addItem(String text) {
                FakeServerInternal.getGlobalFakeServer().addItem(text);
            }
        };
    }

}
