package il.co.noamsl.lostnfound.subScreens.itemsFeed;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.item.LfItem;
import il.co.noamsl.lostnfound.item.NoamImage;
import il.co.noamsl.lostnfound.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.subScreens.PublishedItemActivity;

/**
 * Created by noams on 03/11/2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Loadable, ItemReceiver<LfItemImpl> {
    private ItemsBulk itemsBulk;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading; //careful! should be set only using setter otherwise causing lack of persistence
    private final int VISIBLE_THRESHOLD = 20;
    private final Activity parentActivity;
    private RecyclerView recyclerView;

    private void initOnLoadMoreListener( ) {
        this.onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                new Thread(new Runnable() {
                    public void run() {
                        itemsBulk.requestMoreItems();
                        MyRecyclerAdapter.this.setLoaded();
                    }
                }).start();

            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onItemArrived(LfItemImpl item) {
        if(item==null){
            MyRecyclerAdapter.this.setIsLoading(false);
        }
        myNotifyChange();
    }

    private void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
//        myNotifyChange();
    }

    private void myNotifyChange() {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged(); //// FIXME: 05/11/2017 not efficient
            }
        });

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView itemImage;
        public TextView itemTitle;
        private Long itemId = null;

        public ViewHolder(final View itemView, final Activity parent) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(parent, PublishedItemActivity.class);
                    intent.putExtra(PublishedItemActivity.ARG_ITEM_ID, (long)itemId);
                    parent.startActivity(intent);

/*
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
*/

                }
            });
        }

        public void updateFields(NoamImage image, String title) {
            itemImage.setImageDrawable(image.getDrawable());
            itemTitle.setText(title);

        }

        public void setItemId(long itemId) {
            this.itemId = itemId;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_loading_items);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(ItemsBulk itemsBulk, RecyclerView recyclerView, Activity parent) {
        this.itemsBulk = itemsBulk;
        this.itemsBulk.setItemReceiver(this);
        initLoadingMechanism(recyclerView);
        initOnLoadMoreListener();
        fillFirstItems();
        this.parentActivity = parent;
    }

    private void fillFirstItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyRecyclerAdapter.this.setIsLoading(true);
                itemsBulk.requestMoreItems();

            }
        }).start();
    }

    private void initLoadingMechanism(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        itemsBulk.setRequester(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("Noam", "Scrolled");
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    if (onLoadMoreListener != null) {
                        setIsLoading(true);
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
                    .inflate(R.layout.card_layout, parent, false); //careful
            return new ViewHolder(v,parentActivity);
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
        if (holder instanceof ViewHolder) {
            LfItem itemInPosition = itemsBulk.get(position);
            ViewHolder myHolder = (ViewHolder) holder;
            myHolder.updateFields(itemInPosition.getMainImage(), "Title: " + itemInPosition.getName());
            myHolder.setItemId(itemInPosition.getId());

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("noamd", "getItemCount: "+itemsBulk.getItemCount());
//// FIXME: 15/11/2017 careful
        //fixme danger(with threads no persistence)
        return isLoading? itemsBulk.getItemCount()+1: itemsBulk.getItemCount();
//        return itemsBulk.getItemCount()+1;
    }

    public void setLoaded() {
        setIsLoading(false);
    }

}