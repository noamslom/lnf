package il.co.noamsl.lostnfound.screens.itemsFeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.external.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.screens.EditItemActivity;
import il.co.noamsl.lostnfound.screens.PublishedItemActivity;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ItemsFeedFragment extends Fragment implements ItemReceiver<Boolean>, ItemOpener {
    private static final String TAG = "ItemsFeedFragment";

    private static final String ARG_ITEMS_BULK = "itemsBulk";
    private static final int REQUEST_ITEM_CHANGE = 1;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter; //fixme
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemsBulk itemsBulk;
    private ItemsStateListener itemsStateListener;

    public ItemsFeedFragment() {
        // Required empty public constructor
    }

    public static ItemsFeedFragment newInstance(ItemsBulk itemsBulk, ItemsStateListener itemsStateListener) {
        //// FIXME: 18/11/2017 not saving parent
        ItemsFeedFragment fragment = new ItemsFeedFragment();
        fragment.setItemsStateListener(itemsStateListener);
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEMS_BULK, itemsBulk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {//careful
            return;
        }

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_my_items);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyRecyclerAdapter(itemsBulk, mRecyclerView, getActivity(), getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        initSwipeToRefresh();
    }

    private void initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refresh();
                    }
                }
        );

    }

    private void refresh() {
        mAdapter.clear();
        ServiceLocator.getRepository().clearItems();
        mAdapter.reloadByCurrentFilter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemsBulk = getArguments().getParcelable(ARG_ITEMS_BULK);
        }
        itemsBulk.setItemsStateListener(itemsStateListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_feed, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void setItemsStateListener(ItemsStateListener itemsStateListener) {
        this.itemsStateListener = itemsStateListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void filter(ItemsQuery filter) {
        mAdapter.filter(filter);
    }

    @Override
    public void onItemArrived(Boolean item) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRequestFailure() {
        Log.e(TAG, "not supposed to be here", new IllegalStateException());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == REQUEST_ITEM_CHANGE && resultCode == Activity.RESULT_OK) {
            refresh();
        }

    }

    @Override
    public void openItem(boolean isMyItem, Integer itemId) {
        if (isMyItem) {
            Intent intent = new Intent(getContext(), EditItemActivity.class);
            intent.putExtra(EditItemActivity.ARG_ITEM_ID, itemId);
            intent.putExtra(EditItemActivity.ARG_MODE, EditItemActivity.Mode.EDIT.ordinal());
            startActivityForResult(intent, REQUEST_ITEM_CHANGE);
        } else {
            Intent intent = new Intent(getContext(), PublishedItemActivity.class);
            intent.putExtra(PublishedItemActivity.ARG_ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    public void resetAndreload() {
        mAdapter.clear();
        mAdapter.reloadByCurrentFilter(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
