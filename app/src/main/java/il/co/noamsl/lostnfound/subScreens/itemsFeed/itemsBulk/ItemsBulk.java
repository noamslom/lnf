package il.co.noamsl.lostnfound.subScreens.itemsFeed.itemsBulk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.co.noamsl.lostnfound.dataTransfer.DataPosition;
import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.item.LfItem;
import il.co.noamsl.lostnfound.repository.RepositoryImpl;
import il.co.noamsl.lostnfound.dataTransfer.Request;
import il.co.noamsl.lostnfound.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.Loadable;

/**
 * Created by noams on 05/11/2017.
 */

public class ItemsBulk implements Parcelable, ItemReceiver<LfItem> {
    private int mData; //// FIXME: 05/11/2017 delete this
    private List<LfItem> savedItems;
    private static final int ITEMS_PER_REQUEST = 30;
    private RepositoryImpl repository;
    private RequestAgent requestAgent;
    private Loadable requester;
    private ItemReceiver<LfItem> itemReceiver;

    public ItemsBulk(RepositoryImpl repository, Loadable requester) {
        this.repository = repository;
        this.savedItems = Collections.synchronizedList(new ArrayList<LfItem>());
        requestAgent = new RequestAgent();
        this.requester=requester;
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
    }

/*
    public LfItemImpl get(int position) {
        throw new UnsupportedOperationException("Not imp yet");
    }
*/

    public int getItemCount() {
        return savedItems.size();
    }

    public LfItem get(int position) {
        if(position>=savedItems.size())
            return null;
        return savedItems.get(position);
    }

    public void setRequester(Loadable requester) {
        this.requester = requester;
    }

    public void requestMoreItems() {
        DataPosition<LfItem> lastItemDataPosition;
        if(savedItems.size()!=0){
            lastItemDataPosition = new DataPosition<LfItem>(savedItems.get(savedItems.size()-1));
        }
        else{
            lastItemDataPosition = new DataPosition<>(null);
        }
        repository.requestItems(new Request<LfItem>(this,lastItemDataPosition),null);//// FIXME: 13/11/2017 use request agent preferred
//        if(requester!=null){
//            requester.setLoaded();
//        }

    }

    @Override
    public void onItemArrived(LfItem item) {
        savedItems.add(item);
        if(itemReceiver!=null){
            itemReceiver.onItemArrived(item);
        }
        requestAgent.next();
    }

    public void setItemReceiver(ItemReceiver itemReceiver) {
        this.itemReceiver = itemReceiver;
    }
}
