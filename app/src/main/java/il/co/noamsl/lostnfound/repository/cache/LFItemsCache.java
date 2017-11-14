package il.co.noamsl.lostnfound.repository.cache;

import java.util.HashMap;

import il.co.noamsl.lostnfound.item.LFItem;

/**
 * Created by noams on 14/11/2017.
 */

public class LFItemsCache {
    private HashMap<Long, LFItem> items;

    public void add(LFItem item) {
        items.put(item.getId(), item);
    }

    public LFItem getItem(long id) {
        return items.get(id);
    }
}
