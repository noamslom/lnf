package il.co.noamsl.lostnfound.serverInterface.fake;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.item.FakeImage;
import il.co.noamsl.lostnfound.item.Item;

/**
 * Created by noams on 05/11/2017.
 */

class ItemsDB {
    private List<Item> items;

    public ItemsDB() {
        items = Collections.synchronizedList(new ArrayList<Item>());
        for (int i = 0; i < 100; i++) {
            Item item1 = new Item(i,"Wal"+i,"Great Wallet, brown",null,null,new FakeImage());
            items.add(item1);
        }
    }

    public Item getItem(int i) {
        if (i >= items.size()) {
            return null;
        }
        return items.get(i);
    }
    public void addItem(Item item){
        items.add(item);
    }

    public Item getItemById(long itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public void addItem(String text) {
        addItem(new Item(items.size(),text,null,null,null,new FakeImage()));
    }
}
