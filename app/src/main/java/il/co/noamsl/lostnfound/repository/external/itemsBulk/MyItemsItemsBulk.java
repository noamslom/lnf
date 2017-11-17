package il.co.noamsl.lostnfound.repository.external.itemsBulk;

import il.co.noamsl.lostnfound.repository.Repository;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.screens.itemsFeed.Loadable;
import il.co.noamsl.lostnfound.webService.dataTransfer.DataPosition;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;

public class MyItemsItemsBulk extends ItemsBulk {
    public MyItemsItemsBulk(Repository repository) {
        super(repository, null);
    }

    @Override
    public void requestMoreItems() {
        DataPosition<LfItem> lastItemDataPosition;
        if(storage.size(currentFilter)!=0){
            lastItemDataPosition = new DataPosition<LfItem>(repository.getItemById(storage.getLast(currentFilter)));
        }
        else{
            lastItemDataPosition = new DataPosition<>(null);
        }
        ItemsQuery query = new ItemsQuery(null, null, null, null, repository.getLoggedInUserId());
        repository.requestItems(new Request<LfItem>(this,lastItemDataPosition,query),null);//// FIXME: 13/11/2017 use request agent preferred

    }


}
