package il.co.noamsl.lostnfound;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import il.co.noamsl.lostnfound.serverInterface.NoamServerExternal;
import il.co.noamsl.lostnfound.serverInterface.fake.FakeServerHelperFactory;
import il.co.noamsl.lostnfound.subScreens.itemsFeed.ItemsFeedFragment;
import il.co.noamsl.lostnfound.subScreens.MainFeedFragment;
import il.co.noamsl.lostnfound.subScreens.MyItemsFragment;
import il.co.noamsl.lostnfound.subScreens.SettingsFragment;

public class MainActivity extends AppCompatActivity implements
        MainFeedFragment.OnFragmentInteractionListener,
        MyItemsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        ItemsFeedFragment.OnFragmentInteractionListener {
    private static NoamServerExternal server = null;
    private static Context context_remove; //// FIXME: 05/11/2017

    public static NoamServerExternal getServer() {
        return server;
    }

    private MyItemsFragment myItemsFragment = null;
    private MainFeedFragment mainFeedFragment = null;
    private SettingsFragment settingsFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    setFragmentToMainFeed();
                    return true;
                case R.id.navigation_my_items:
                    setFragmentToMyItems();
                    return true;
                case R.id.navigation_settings:
                    setFragmentToSettings();
                    return true;
            }
            return false;
        }


    };

    private void setFragmentToSettings() {
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }
        replaceFragment(settingsFragment);
    }

    private void setFragmentToMainFeed() {
        if (mainFeedFragment == null) {
            mainFeedFragment = new MainFeedFragment();
        }
        replaceFragment(mainFeedFragment);

    }

    private void setFragmentToMyItems() {
        if (myItemsFragment == null) {
            myItemsFragment = new MyItemsFragment();
        }
        replaceFragment(myItemsFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initServer();

        initFragment(savedInstanceState);

        context_remove = getApplicationContext();


    }

    private void initServer() {
        server = FakeServerHelperFactory.newInstance();
    }

    private void initFragment(Bundle savedInstanceState) {
        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }
        // Create a new Fragment to be placed in the activity layout
        mainFeedFragment = new MainFeedFragment();
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        mainFeedFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout_fragment_container, mainFeedFragment).commit();
    }


    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.frame_layout_fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO handle later
    }

    public static Context getContextRemoveThisMethod() {
        return context_remove;
    }
}