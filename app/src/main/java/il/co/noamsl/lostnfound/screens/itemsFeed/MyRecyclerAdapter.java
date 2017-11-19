package il.co.noamsl.lostnfound.screens.itemsFeed;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.external.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.repository.external.itemsBulk.MyItemsItemsBulk;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;

/**
 * Created by noams on 03/11/2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemReceiver<LfItem> {
    private static final String TAG = "MyRecyclerAdapter";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VISIBLE_THRESHOLD = 10;
    private final LiveData<Activity> parentActivity;
    private final Context context;
    private final int NOTIFY_FREQ = 500;
    private final ItemOpener itemOpener;
    private ItemsBulk itemsBulk;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private volatile Boolean isLoading = false;
    private RecyclerView recyclerView;
    private volatile boolean changesToNotifyMade = false;
    private volatile boolean loadedAll = false;
    private ItemReceiver<Boolean> containerItemReceiver = null;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(ItemsBulk itemsBulk, RecyclerView recyclerView, Activity parent, Context context, ItemOpener itemOpener) {
        this.itemsBulk = itemsBulk;
        this.context = context;
        this.itemOpener = itemOpener;
        this.itemsBulk.setItemReceiver(this);
        this.recyclerView = recyclerView;
        this.parentActivity = Util.createLiveData(parent);

        initLoadingMechanism(recyclerView);
        initOnLoadMoreListener();
        initNotifier();

        filter(new ItemsQuery("", "", null, true, null)); //fixme this is default query


    }

    private void initOnLoadMoreListener() {
        this.onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestMoreItems();
            }
        };
    }

    private void requestMoreItems() {
        if (getIsLoading())
            return;

        setIsLoading(true);
        itemsBulk.requestMoreItems();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public synchronized void onItemArrived(LfItem item) {
        if (item == null) {
            MyRecyclerAdapter.this.setIsLoading(false);
            loadedAll = true;
            if (containerItemReceiver != null) {
                containerItemReceiver.onItemArrived(true);
            }

        }
        myNotifyChange(false);
    }

    @Override
    public synchronized void onRequestFailure() {
        onItemArrived(null);
        Util.MyToast.show(recyclerView.getContext(), "Unable to load items", Toast.LENGTH_SHORT);
    }

    private void postToNextNotify() {
        changesToNotifyMade = true;
    }

    private void initNotifier() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (changesToNotifyMade) {
                            myRealNotifyChange();
                            changesToNotifyMade = false;
                        }
                    }

                    private void myRealNotifyChange() {
                        Runnable notifier = new Runnable() {
                            @Override
                            public void run() {
                                synchronized (itemsBulk) {
                                    synchronized (isLoading) {
                                        notifyDataSetChanged();
                                        Log.d(TAG, "notifyDataSetChanged");

                                    }
                                }
                            }
                        };
                        recyclerView.post(notifier);
                    }
                });
            }
        }, 100, NOTIFY_FREQ);

    }

    private synchronized void myNotifyChange(final boolean all) {
        if (!loadedAll) {
            postToNextNotify();
        }
        //fixme not doing anything here

    }

    public synchronized boolean getIsLoading() {
        return isLoading;
    }

    private synchronized void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
        myNotifyChange(true); // FIXME: 16/11/2017 enable this
    }

    public void filter(ItemsQuery filter) {
        Log.d(TAG, "filter: " + filter);

        loadedAll = false;
        itemsBulk.filter(filter);

        myNotifyChange(true);
        fillFirstItems();

    }

    public void clear() {
        synchronized (this) {
            itemsBulk.clear();
            notifyDataSetChanged();
        }
    }

    public void reloadByCurrentFilter(ItemReceiver<Boolean> refreshItemReceiver) {
        this.containerItemReceiver = refreshItemReceiver;
        filter(itemsBulk.getCurrentFilter());
    }

    private void fillFirstItems() {
        requestMoreItems();

/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyRecyclerAdapter.this.setIsLoading(true);
                itemsBulk.requestMoreItems();

            }
        }).start(); //// FIXME: 16/11/2017 think if shuold be thread
*/
    }

    private void initLoadingMechanism(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private final int MAX_SCROLL_FREQ = 500;
            private volatile boolean scrolling = false;

            public synchronized boolean isScrolling() {
                return scrolling;
            }

            public synchronized void setScrolling(boolean scrolling) {
                this.scrolling = scrolling;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isScrolling())
                    return;
                setScrolling(true);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setScrolling(false);
                    }
                }, MAX_SCROLL_FREQ);


                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!getIsLoading() && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public int getItemViewType(int position) {
        return itemsBulk.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        // set the view's size, margins, paddings and layout parameters
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_layout, parent, false); //careful
            //fixme not ideal to use instanceof here, not modular
            return new ItemViewHolder(v, parentActivity.getValue(), itemsBulk instanceof MyItemsItemsBulk, context, itemOpener);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder instanceof ItemViewHolder) {
            LfItem itemInPosition = itemsBulk.get(position);
            ItemViewHolder myHolder = (ItemViewHolder) holder;
            myHolder.updateFields(itemInPosition.getDrawablePicture(context), itemInPosition.getName(),
                    itemInPosition.getDescription(), itemInPosition.getLocation());
            myHolder.setItemId(itemInPosition.getId());

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public synchronized int getItemCount() {
//// FIXME: 15/11/2017 careful
        //fixme danger(with threads no persistence)
        synchronized (itemsBulk) {
            synchronized (isLoading) {
//                return getIsLoading() ? itemsBulk.getItemCount() + 1 : itemsBulk.getItemCount();
                return itemsBulk.getItemCount() + 1;
            }
        }

//        return itemsBulk.getItemCount()+1;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemOpener itemOpener;
        // each data item is just a string in this case
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemDescription;
        private TextView itemLocation;
        private Integer itemId = null;
        private boolean isMyItem;


        public ItemViewHolder(final View itemView, final Activity parent, final boolean isMyItem, Context context, ItemOpener itemOpener) {
            super(itemView);
            this.isMyItem = isMyItem;
            this.itemOpener = itemOpener;

            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemDescription = (TextView) itemView.findViewById(R.id.item_card_tv_item_details);
            itemLocation = (TextView) itemView.findViewById(R.id.item_card_tv_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ItemViewHolder.this.itemOpener != null) {
                        ItemViewHolder.this.itemOpener.openItem(isMyItem, itemId);

                    }

/*
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
*/

                }
            });
        }


        public void updateFields(Drawable picture, String title, String description, String address) {
            itemDescription.setText(description);
            itemLocation.setText(address);
            itemTitle.setText(title);
            itemImage.setImageDrawable(picture);

        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final int REFRESH_FREQ = 500;
        private ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_loading_items);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (isLoading) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                            }
                        });
                    }
                }
            }, 100, REFRESH_FREQ);

        }
    }

}