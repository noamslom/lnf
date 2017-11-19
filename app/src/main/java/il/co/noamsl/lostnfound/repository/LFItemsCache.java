package il.co.noamsl.lostnfound.repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import il.co.noamsl.lostnfound.repository.cache.Cache;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;


public class LFItemsCache extends Cache<LfItem> {
    private static final String TAG = "LFItemsCache";
    public List<LfItem> get(ItemsQuery filter) {
        List<LfItem> filteredItems = new ArrayList<>();
        Collection<LfItem> allItems = hashMap.values();

        for (LfItem item : allItems) {
            if (matches(item, filter)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private boolean matches(LfItem item, ItemsQuery filter) {
        boolean matches = false;
        if(filter.getOwner()!=null && item.getOwner()!=null){
            matches |= filter.getOwner().equals(item.getOwner());
        }
        if(filter.getName()!=null && item.getName()!=null){
            matches |= item.getName().toLowerCase().contains(filter.getName().toLowerCase());
        }
        if(filter.getDescription()!=null && item.getDescription()!=null){
            matches |= item.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase());

        }
        if(filter.getLocation()!=null && item.getLocation()!=null){
            matches |= item.getLocation().toLowerCase().contains(filter.getLocation().toLowerCase());

        }
        if(filter.isAFound()!=null){
            matches&=(filter.isAFound()==item.isAFound());
        }
        if(filter.isRelevant()!=null){
            matches&=(filter.isRelevant()==item.getRelevant());
        }
        return matches;

    }


}
