package il.co.noamsl.lostnfound.serverInterface.fake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.co.noamsl.lostnfound.item.FakeItem;

/**
 * Created by noams on 05/11/2017.
 */

class ItemsDB {
    private List<FakeItem> items;

    public ItemsDB() {
        items = Collections.synchronizedList(new ArrayList<FakeItem>());
        for (int i = 0; i < 100; i++) {
            FakeItem item1 = new FakeItem(i,"Wal"+i,"Great Wallet, brown",null,null,new FakeImage());
            items.add(item1);
        }
    }

    public FakeItem getItem(int i) {
        if (i >= items.size()) {
            return null;
        }
        return items.get(i);
    }
    public void addItem(FakeItem item){
        items.add(item);
    }

    public FakeItem getItemById(long itemId) {
        for (FakeItem item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public void addItem(String text) {
        addItem(new FakeItem(items.size(),text,null,null,null,new FakeImage()));
    }
}
