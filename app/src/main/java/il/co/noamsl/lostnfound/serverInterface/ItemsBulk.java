package il.co.noamsl.lostnfound.serverInterface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.co.noamsl.lostnfound.item.Item;
import il.co.noamsl.lostnfound.item.RequestAgent;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.Loadable;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.MyRecyclerAdapter;

/**
 * Created by noams on 05/11/2017.
 */

public class ItemsBulk implements Parcelable, ItemReceiver {
    private int mData; //// FIXME: 05/11/2017 delete this
    private volatile int itemCount = 0;
    private List<Item> savedItems;
    private static final int ITEMS_PER_REQUEST = 30;
    private NoamServerInternal server;
    private RequestAgent requestAgent;
    private Loadable requester;
    private ItemReceiver itemReceiver;

    public ItemsBulk(NoamServerInternal server,Loadable requester) {
        this.server = server;
        this.savedItems = Collections.synchronizedList(new ArrayList<Item>());
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
    public Item get(int position) {
        throw new UnsupportedOperationException("Not imp yet");
    }
*/

    public int getItemCount() {
        return itemCount;
    }

    public Item get(int position) {
        if(position>=itemCount)
            return null;
        return savedItems.get(position);
    }

    public void setRequester(Loadable requester) {
        this.requester = requester;
    }

    public void requestMoreItems() {
        requestAgent.addRequested(ITEMS_PER_REQUEST);
        server.requestItems(this, requestAgent);
//        if(requester!=null){
//            requester.setLoaded();
//        }

    }

    @Override
    public void onItemArrived(Item item) {
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
