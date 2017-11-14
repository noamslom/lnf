package il.co.noamsl.lostnfound.serverInterface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.repository.RepositoryImpl;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.Loadable;

/**
 * Created by noams on 05/11/2017.
 */

public class ItemsBulk implements Parcelable, ItemReceiver<FakeItem> {
    private int mData; //// FIXME: 05/11/2017 delete this
    private volatile int itemCount = 0;
    private List<FakeItem> savedItems;
    private static final int ITEMS_PER_REQUEST = 30;
    private RepositoryImpl repository;
    private RequestAgent requestAgent;
    private Loadable requester;
    private ItemReceiver<FakeItem> itemReceiver;

    public ItemsBulk(RepositoryImpl repository, Loadable requester) {
        this.repository = repository;
        this.savedItems = Collections.synchronizedList(new ArrayList<FakeItem>());
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
    public FakeItem get(int position) {
        throw new UnsupportedOperationException("Not imp yet");
    }
*/

    public int getItemCount() {
        return itemCount;
    }

    public FakeItem get(int position) {
        if(position>=itemCount)
            return null;
        return savedItems.get(position);
    }

    public void setRequester(Loadable requester) {
        this.requester = requester;
    }

    public void requestMoreItems() {
        requestAgent.addRequested(ITEMS_PER_REQUEST);
        repository.requestItems(this, null);//// FIXME: 13/11/2017 use request agent preferred
//        if(requester!=null){
//            requester.setLoaded();
//        }

    }

    @Override
    public void onItemArrived(FakeItem item) {
        savedItems.add(item);
        itemCount++;
        if(itemReceiver!=null){
            itemReceiver.onItemArrived(item);
        }
        requestAgent.next();
    }

    public void setItemReceiver(ItemReceiver itemReceiver) {
        this.itemReceiver = itemReceiver;
    }
}
