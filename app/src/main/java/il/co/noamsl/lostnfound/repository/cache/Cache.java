package il.co.noamsl.lostnfound.repository.cache;

import java.util.HashMap;

/**
 * Created by noams on 14/11/2017.
 */

public class Cache<T extends Cacheable> {
    private HashMap<String, T> hashMap;

    public Cache() {
        hashMap = new HashMap<>();
    }

    public void add(T t) {
        hashMap.put(t.getCacheId(), t);
    }

    public T get(String id) {
        return hashMap.get(id);
    }
}
