package il.co.noamsl.lostnfound.screens.itemsFeed;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.screens.itemsFeed.itemsBulk.ItemsBulk;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ItemsFeedFragment extends Fragment {
    private static final String TAG = "ItemsFeedFragment";

    private static final String ARG_ITEMS_BULK = "itemsBulk";
    private RecyclerView mRecyclerView;
    private LiveData<RecyclerView.Adapter> mAdapter; //fixme
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    private ItemsBulk itemsBulk;
    public ItemsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null){//careful
            Log.d(TAG, "onViewCreated: savedInstanceState = " + savedInstanceState);
            return;
        }

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_my_items);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        MutableLiveData<RecyclerView.Adapter> adapter = new MutableLiveData<RecyclerView.Adapter>();
        adapter.setValue(new MyRecyclerAdapter(itemsBulk,mRecyclerView,getActivity()));

        // specify an adapter (see also next example)
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter.getValue());

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemsBulk = getArguments().getParcelable(ARG_ITEMS_BULK);
        }

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
    public static ItemsFeedFragment newInstance(ItemsBulk itemsBulk) {
        ItemsFeedFragment fragment = new ItemsFeedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEMS_BULK,itemsBulk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
