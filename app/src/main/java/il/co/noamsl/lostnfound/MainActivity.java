package il.co.noamsl.lostnfound;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import il.co.noamsl.lostnfound.repository.external.RepositoryExternal;
import il.co.noamsl.lostnfound.screens.MainFeedFragment;
import il.co.noamsl.lostnfound.screens.MyItemsFragment;
import il.co.noamsl.lostnfound.screens.SettingsFragment;
import il.co.noamsl.lostnfound.screens.itemsFeed.ItemsFeedFragment;


public class MainActivity extends AppCompatActivity implements
        MainFeedFragment.OnFragmentInteractionListener,
        MyItemsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        ItemsFeedFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private MyItemsFragment myItemsFragment = null;
    private MainFeedFragment mainFeedFragment = null;
    private SettingsFragment settingsFragment = null;
    private BottomNavigationView navigation;
    private boolean navigationListening = true;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (!navigationListening)
                return true;
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

    public static RepositoryExternal getExternalRepository() {
        return ServiceLocator.getExternalRepository();
    }

    private void setFragmentToSettings() {
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance(this);
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


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        initFragment(savedInstanceState);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNavigatorPosition();
    }

    private void updateNavigatorPosition() {
        Fragment currentFragment = getCurrentFragment();
        int id;

        if (currentFragment instanceof MainFeedFragment) {
            id = R.id.navigation_feed;
        } else if (currentFragment instanceof MyItemsFragment) {
            id = R.id.navigation_my_items;
        } else if (currentFragment instanceof SettingsFragment) {
            id = R.id.navigation_settings;
        } else {
            throw new IllegalStateException("fragment should be 1 of three main,my items,settings");
        }
        setNavigationListening(false);
        navigation.setSelectedItemId(id);
        setNavigationListening(true);


    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_layout_fragment_container);
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
    }

    public void setNavigationListening(boolean navigationListening) {
        this.navigationListening = navigationListening;
    }

    public void logout() {
        ServiceLocator.getRepository().clearLoggedInUser();
        ServiceLocator.clearGlobalRepository();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
