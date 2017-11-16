package il.co.noamsl.lostnfound.repository;

import android.util.Log;

import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.webService.WebService;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

/**
 * Created by noams on 14/11/2017.
 */

public class LoggedInUserRepository {
    private static final String TAG = "LoggedInUserRepository";
    private final WebService webService;
    private User loggedInUser;

    public LoggedInUserRepository(WebService webService) {
        this.webService = webService;
    }

    public int getLoggedInUserId() {
        return loggedInUser.getUserid();
    }

    public void setLoggedInUser(final ItemReceiver<User> itemReceiver, int userId) {
        webService.getUserById(new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                loggedInUser = user;
                Log.d(TAG, "onItemArrived: user = " + user);
                itemReceiver.onItemArrived(user);
            }
        }, userId);
    }

}
