package il.co.noamsl.lostnfound.subScreens.itemsFeed;

import android.support.v4.app.Fragment;

import il.co.noamsl.lostnfound.serverInterface.ItemsBulk;

/**
 * Created by noams on 05/11/2017.
 */

public class ItemFeed {
    private ItemsFeedFragment itemsFeedFragment;

    public ItemFeed(Fragment parent, int container, ItemsBulk itemsBulk) {
        // Create a new Fragment to be placed in the activity layout
        itemsFeedFragment = ItemsFeedFragment.newInstance(itemsBulk);
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        //careful, proboably not right programming to put here. Consider inheritance
//        itemsFeedFragment.setArguments(parent.getActivity().getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        parent.getChildFragmentManager().beginTransaction()
                .add(container, itemsFeedFragment).commit();
    }

}

