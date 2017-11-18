package il.co.noamsl.lostnfound.screens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.screens.itemsFeed.ItemFeed;
import il.co.noamsl.lostnfound.screens.itemsFeed.ItemsStateListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyItemsFragment extends Fragment implements ItemsStateListener {
    private static final String TAG = "MyItemsFragment";
    private OnFragmentInteractionListener mListener;
    private ItemFeed itemFeed ;
    private TextView tvNoItems;
    boolean noItems = true;
//    private ItemsFeedFragment itemsFeedFragment;
    public MyItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }

        itemFeed = new ItemFeed(this,R.id.fl_feed_containter, ServiceLocator.getExternalRepository().getMyItemsItemsBulk(),this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNoItems = (TextView) getActivity().findViewById(R.id.my_items_tv_no_items);
        updateNoItemsVisibility();

    }

    public void onNofItemsChange(int nofItems){
        noItems = (nofItems<=0);
        if(tvNoItems!=null){
            updateNoItemsVisibility();
        }
    }

    private void updateNoItemsVisibility() {
        tvNoItems.setVisibility(noItems ? View.VISIBLE : View.GONE);
    }

    /* private void initItemFeed(Bundle savedInstanceState) {
         if (savedInstanceState != null) {
             return;
         }
         // Create a new Fragment to be placed in the activity layout
         itemsFeedFragment = new ItemsFeedFragment();
         // In case this activity was started with special instructions from an
         // Intent, pass the Intent's extras to the fragment as arguments
         itemsFeedFragment.setArguments(getActivity().getIntent().getExtras());

         // Add the fragment to the 'fragment_container' FrameLayout
         getChildFragmentManager().beginTransaction()
                 .add(R.id.fl_feed_containter, itemsFeedFragment).commit();
     }
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_items, container, false);
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
