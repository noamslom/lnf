package il.co.noamsl.lostnfound.repository.cache;

import java.util.HashMap;
import java.util.List;

import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;

public class Cache<T extends Cacheable> {
    protected HashMap<String, T> hashMap;

    public Cache() {
        hashMap = new HashMap<>();
    }

    public void add(T t) {
        hashMap.put(t.getCacheId(), t);
    }

    public T get(String id) {
        return hashMap.get(id);
    }

    public void updateItem(T newItem) {
        hashMap.put(newItem.getCacheId(), newItem);
    }

    @Override
    public String toString() {
        return "Cache{" +
                "hashMap=" + hashMap +
                '}';
    }
}
