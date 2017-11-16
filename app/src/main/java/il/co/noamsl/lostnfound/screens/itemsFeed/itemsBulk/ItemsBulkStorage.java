package il.co.noamsl.lostnfound.screens.itemsFeed.itemsBulk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;

/**
 * Created by noams on 16/11/2017.
 */

public class ItemsBulkStorage {
    //query --> list of all items ids for this query
    private Hashtable<ItemsQuery,List<Integer>> itemsIdTable;

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
        idList.add(id);
    }
}
