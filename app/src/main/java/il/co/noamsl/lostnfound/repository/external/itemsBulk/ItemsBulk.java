package il.co.noamsl.lostnfound.repository.external.itemsBulk;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import il.co.noamsl.lostnfound.repository.Repository;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.screens.itemsFeed.ItemsStateListener;
import il.co.noamsl.lostnfound.webService.dataTransfer.DataPosition;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;


public class ItemsBulk implements Parcelable, ItemReceiver<LfItem> {
    public static final Parcelable.Creator<ItemsBulk> CREATOR
            = new Parcelable.Creator<ItemsBulk>() {
        public ItemsBulk createFromParcel(Parcel in) {
            return new ItemsBulk(in);
        }

        public ItemsBulk[] newArray(int size) {
            return new ItemsBulk[size];
        }
    };
    private static final String TAG = "ItemsBulk";
    private static final int ITEMS_PER_REQUEST = 30;
    protected Repository repository;
    protected ItemsQuery currentFilter;
    protected ItemsBulkStorage storage;
    private int mData;
    private RequestAgent requestAgent;
    private ItemReceiver<LfItem> itemReceiver;
    private ItemsStateListener itemsStateListener;


    public ItemsBulk(Repository repository) {
        this.repository = repository;
        this.storage = new ItemsBulkStorage();
        requestAgent = new RequestAgent();

        startReporter();
    }

    private ItemsBulk(Parcel in) {
        mData = in.readInt();
        throw new UnsupportedOperationException("Parcel not imp yet");
    }

    public ItemsQuery getCurrentFilter() {
        return currentFilter;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public synchronized int getItemCount() {
        return storage.size(currentFilter);
    }

    public LfItem get(int position) {
        if (position >= getItemCount())
            return null;
        Integer itemId = storage.getItemId(currentFilter, position);
        return repository.getItemById(itemId);
    }

    public void requestMoreItems() {
        DataPosition<LfItem> lastItemDataPosition;

        if (storage.size(currentFilter) != 0) {
            lastItemDataPosition = new DataPosition<LfItem>(repository.getItemById(storage.getLast(currentFilter)));
        } else {
            lastItemDataPosition = new DataPosition<>(null);
        }
        Log.d(TAG, "requestMoreItems: lastItemDataPosition = " + lastItemDataPosition);
        Log.d(TAG, "requestMoreItems: currentFilter = " + currentFilter);

        repository.requestItems(new Request<LfItem>(this, lastItemDataPosition, currentFilter), null);//// FIXME: 13/11/2017 use request agent preferred

    }

    @Override
    public void onItemArrived(LfItem item) {
        if (item == null) {
            if (itemReceiver != null) {
                itemReceiver.onItemArrived(item);
            }
            return;
        }
        if (relevancyOk(item)) return;
        storage.addItemId(currentFilter, item.getId());
        if (itemsStateListener != null) {
            itemsStateListener.onNofItemsChange(getItemCount());
        }
        if (itemReceiver != null) {
            itemReceiver.onItemArrived(item);
        }
        requestAgent.next();
    }

    private boolean relevancyOk(LfItem item) {
        Boolean filterRelevant = currentFilter.isRelevant();
        if (!item.getRelevant() && filterRelevant != null && filterRelevant) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestFailure() {
        itemReceiver.onRequestFailure();
    }

    public void setItemReceiver(ItemReceiver itemReceiver) {
        this.itemReceiver = itemReceiver;
    }


    public void filter(ItemsQuery filter) {

        currentFilter = filter;
    }

    public void setItemsStateListener(ItemsStateListener itemsStateListener) {
        this.itemsStateListener = itemsStateListener;
    }

    public void clear() {
        storage.clear();
    }


    public void startReporter() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run_timer_report: " + storage.get(currentFilter));
            }
        }, 0, 500);
    }
}
