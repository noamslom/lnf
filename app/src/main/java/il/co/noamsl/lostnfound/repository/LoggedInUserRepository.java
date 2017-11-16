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
    private User loggedInUser = null;

    public LoggedInUserRepository(WebService webService) {
        this.webService = webService;
    }

    public int getLoggedInUserId() {
        return loggedInUser.getUserid();
    }

    public void setLoggedInUser(final ItemReceiver<User> itemReceiver, String credential) {
        webService.getUserByCredential(new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                if (user != null) {
                    loggedInUser = user;
                }
                itemReceiver.onItemArrived(user);
            }
        }, credential);
    }

    public User getUser() {
        return loggedInUser;
    }

    public void update(User user) {
        webService.updateUser(new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                if (user != null) {
                    loggedInUser = user;
                }
            }
        }, user);

    }
}
