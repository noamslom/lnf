package il.co.noamsl.lostnfound.repository.external.itemsBulk;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import il.co.noamsl.lostnfound.webService.dataTransfer.DataPosition;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.repository.Repository;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.screens.itemsFeed.Loadable;

/**
 * Created by noams on 05/11/2017.
 */

public class ItemsBulk implements Parcelable, ItemReceiver<LfItem> {
    private static final String TAG = "ItemsBulk";
    private int tempItemsCount=0;
    private int mData; //// FIXME: 05/11/2017 delete this
    private static final int ITEMS_PER_REQUEST = 30;
    protected Repository repository;
    private RequestAgent requestAgent;
    private Loadable requester;
    private ItemReceiver<LfItem> itemReceiver;
//    private int itemsCount;
protected ItemsQuery currentFilter;
    protected ItemsBulkStorage storage;

    public ItemsBulk(Repository repository, Loadable requester) {
        this.repository = repository;
        this.storage = new ItemsBulkStorage();
        requestAgent = new RequestAgent();
        this.requester=requester;
//        this.itemsCount=0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<ItemsBulk> CREATOR
            = new Parcelable.Creator<ItemsBulk>() {
        public ItemsBulk createFromParcel(Parcel in) {
            return new ItemsBulk(in);
        }

        public ItemsBulk[] newArray(int size) {
            return new ItemsBulk[size];
        }
    };

    private ItemsBulk(Parcel in) {
        mData = in.readInt();
        throw new UnsupportedOperationException("Parcel not imp yet");
    }

/*
    public LfItem get(int position) {
        throw new UnsupportedOperationException("Not imp yet");
    }
*/

    public int getItemCount() {
        return storage.size(currentFilter);
    }

    public LfItem get(int position) {
        if(position>=getItemCount())
            return null;
        Integer itemId = storage.getItemId(currentFilter,position);
        return repository.getItemById(itemId);
    }

    public void setRequester(Loadable requester) {
        this.requester = requester;
    }

    public void requestMoreItems() {
        DataPosition<LfItem> lastItemDataPosition;
        if(storage.size(currentFilter)!=0){
//            lastItemDataPosition = new DataPosition<LfItem>(repository.getItemById(storage.getLast(currentFilter))); // FIXME: 17/11/2017 enable this
            lastItemDataPosition = new DataPosition<>(null);
        }
        else{
            lastItemDataPosition = new DataPosition<>(null);
        }
        repository.requestItems(new Request<LfItem>(this,lastItemDataPosition,currentFilter),null);//// FIXME: 13/11/2017 use request agent preferred
//        if(requester!=null){
//            requester.setLoaded();
//        }

    }

    @Override
    public void onItemArrived(LfItem item) {
        if (item == null) {
            if(itemReceiver!=null){
                itemReceiver.onItemArrived(item);
            }
            return;
        }
        storage.addItemId(currentFilter,item.getId());
//        Log.d("noamd", "itum bulk added" + itemsCount);
//        itemsCount++;
        if(itemReceiver!=null){
            itemReceiver.onItemArrived(item);
        }
        requestAgent.next();
    }

    @Override
    public void onRequestFailure() {
        itemReceiver.onRequestFailure();
    }

    public void setItemReceiver(ItemReceiver itemReceiver) {
        this.itemReceiver = itemReceiver;
    }

/*
    public void temporarilyEmptyList() {
        if(itemsCount==0)
            return;
        tempItemsCount = itemsCount;
    }
*/

/*
    public void restoreList() {
        itemsCount= tempItemsCount;
    }
*/

    public void filter(ItemsQuery filter) {
        Log.d(TAG, "filter: filter = " + filter);

        currentFilter = filter;
    }
}
