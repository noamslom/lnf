package il.co.noamsl.lostnfound.screens.itemsFeed;

import android.support.v4.app.Fragment;

import il.co.noamsl.lostnfound.repository.external.itemsBulk.ItemsBulk;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemsQuery;


public class ItemFeed {
    private ItemsFeedFragment itemsFeedFragment;

    public ItemFeed(Fragment parent, int container, ItemsBulk itemsBulk,ItemsStateListener itemsStateListener) {
        // Create a new Fragment to be placed in the activity layout
        itemsFeedFragment = ItemsFeedFragment.newInstance(itemsBulk,itemsStateListener);
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        //careful, proboably not right programming to put here. Consider inheritance
//        itemsFeedFragment.setArguments(parent.getActivity().getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        parent.getChildFragmentManager().beginTransaction()
                .add(container, itemsFeedFragment).commit();
    }



    public void filter(ItemsQuery itemsQuery) {
        itemsFeedFragment.filter(itemsQuery);
    }

    public void resetAndreload() {
        itemsFeedFragment.resetAndreload();
    }
}

