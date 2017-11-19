package il.co.noamsl.lostnfound.repository.external.itemsBulk;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;


public class ItemsBulkStorage {
    private static final String TAG = "ItemsBulkStorage";
    //query --> list of all items ids for this query
    private Hashtable<ItemsQuery, Set<Integer>> itemsIdTable;

    public ItemsBulkStorage() {
        itemsIdTable = new Hashtable<>();
    }

    public List<Integer> getItemsIdList(ItemsQuery filter) {
        return new ArrayList<>(generateAndGetIdList(filter));
    }

    private Set<Integer> generateAndGetIdList(ItemsQuery filter) {
        Set<Integer> idList = itemsIdTable.get(filter);
        if (idList == null) {
            idList = Collections.synchronizedSet(new HashSet<Integer>());
        }
        return idList;
    }

    public void addItemId(ItemsQuery filter, int id) {
        Set<Integer> idList = generateAndGetIdList(filter);
        itemsIdTable.put(filter, idList);
        idList.add(id);


    }

    public int size(ItemsQuery currentFilter) {
        return getItemsIdList(currentFilter).size();
    }

    public Integer getItemId(ItemsQuery filter, int position) {
        return getItemsIdList(filter).get(position);
    }

    public Integer getLast(ItemsQuery filter) {
        List<Integer> itemsIdList = getItemsIdList(filter);
        return itemsIdList.get(itemsIdList.size() - 1);
    }

    public void clear() {
        itemsIdTable = new Hashtable<>();
    }

    public Set<Integer> get(ItemsQuery currentFilter) {
        if (currentFilter == null) {
            return null;
        }
        return itemsIdTable.get(currentFilter);
    }
}
