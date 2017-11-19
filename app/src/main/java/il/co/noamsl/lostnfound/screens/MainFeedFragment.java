package il.co.noamsl.lostnfound.screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.ToggleButton;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.screens.itemsFeed.ItemFeed;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFeedFragment extends Fragment {
    private static final String TAG = "MainFeedFragment";
    private static final boolean FOUND_TOGGLE_VALUE = false;
    private OnFragmentInteractionListener mListener;
    private ItemFeed itemFeed ;
    private SearchView svFilter;
    private ToggleButton tgbLostOrFound;


    private FloatingActionButton fabNewItem;
    public MainFeedFragment() {
        // Required empty public constructor
    }

    public void newItem(){
//        Util.MyToast.show(getContext(), "Clicked", MyToast.LENGTH_SHORT);
        Intent intent = new Intent(getContext(),EditItemActivity.class);
        intent.putExtra(EditItemActivity.ARG_MODE, EditItemActivity.Mode.ADD.ordinal());
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
        itemFeed = new ItemFeed(this,R.id.main_feed_fl_feed_containter, MainActivity.getExternalRepository().getAllItemsItemsBulk(),null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //// FIXME: 05/11/2017 check if needed here or in orCreate
        fabNewItem = (FloatingActionButton) getActivity().findViewById(R.id.fab_new_item);
        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem();
            }
        });

        svFilter = (SearchView) getActivity().findViewById(R.id.main_feed_sv_filter);
        svFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter();
                return false;
            }
        });
        tgbLostOrFound = (ToggleButton) getActivity().findViewById(R.id.toggleButton);
        tgbLostOrFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });
        
    }

    private void filter() {
        itemFeed.filter(new ItemsQuery(svFilter.getQuery()+"", svFilter.getQuery()+"",null,isToggleButtonAFound(),true));
    }

    private boolean isToggleButtonAFound() {
        return tgbLostOrFound.isChecked() == FOUND_TOGGLE_VALUE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_feed, container, false);
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
