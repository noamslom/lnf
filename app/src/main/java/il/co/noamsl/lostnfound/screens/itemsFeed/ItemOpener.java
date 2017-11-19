package il.co.noamsl.lostnfound.screens.itemsFeed;

import android.app.Activity;
import android.content.Intent;

import il.co.noamsl.lostnfound.screens.EditItemActivity;
import il.co.noamsl.lostnfound.screens.PublishedItemActivity;

/**
 * Created by noams on 19/11/2017.
 */

public interface ItemOpener {
    void openItem(boolean isMyItem, Integer itemId);
}
