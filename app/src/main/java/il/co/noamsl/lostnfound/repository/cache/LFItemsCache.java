package il.co.noamsl.lostnfound.repository.cache;

import java.util.HashMap;

import il.co.noamsl.lostnfound.repository.item.LfItem;

/**
 * Created by noams on 14/11/2017.
 */

public class LFItemsCache {
    private HashMap<String, LfItem> items;

    public LFItemsCache() {
        items = new HashMap<>();
    }

    public void add(LfItem item) {
        items.put(item.getId()+"", item);
    }

    public LfItem getItem(String id) {
        return items.get(id);
    }
}
