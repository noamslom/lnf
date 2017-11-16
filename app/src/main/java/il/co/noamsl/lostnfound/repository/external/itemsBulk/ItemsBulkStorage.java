package il.co.noamsl.lostnfound.repository.external.itemsBulk;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;

/**
 * Created by noams on 16/11/2017.
 */

public class ItemsBulkStorage {
    private static final String TAG = "ItemsBulkStorage";
    //query --> list of all items ids for this query
    private Hashtable<ItemsQuery,List<Integer>> itemsIdTable;

    public ItemsBulkStorage() {
        itemsIdTable = new Hashtable<>();
    }

    public List<Integer> getItemsIdList(ItemsQuery filter){
        return generateAndGetIdList(filter);
    }

    private List<Integer> generateAndGetIdList(ItemsQuery filter) {
        List<Integer> idList = itemsIdTable.get(filter);
        if(idList==null){
            idList =  Collections.synchronizedList(new ArrayList<Integer>());
        }
        return idList;
    }

    public void addItemId(ItemsQuery filter, int id) {
        List<Integer> idList = generateAndGetIdList(filter);
        itemsIdTable.put(filter, idList);
        idList.add(id);
        Log.d(TAG, "addItemId: added item id = "+id+"idList = " + idList);

    }

    public int size(ItemsQuery currentFilter) {
        return getItemsIdList(currentFilter).size();
    }

    public Integer getItemId(ItemsQuery filter, int position) {
        return getItemsIdList(filter).get(position);
    }

    public Integer getLast(ItemsQuery filter) {
        List<Integer> itemsIdList = getItemsIdList(filter);
        return itemsIdList.get(itemsIdList.size()-1);
    }
}
