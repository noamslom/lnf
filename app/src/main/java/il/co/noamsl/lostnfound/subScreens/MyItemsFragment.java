package il.co.noamsl.lostnfound.subScreens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.ItemFeed;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyItemsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ItemFeed itemFeed ;
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
        itemFeed = new ItemFeed(this,R.id.fl_feed_containter, MainActivity.getExternalRepository().getMyItemsItemsBulk());

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
