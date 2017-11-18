package il.co.noamsl.lostnfound.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import junit.framework.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.webService.WebService;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

/**
 * Created by noams on 14/11/2017.
 */

public class LoggedInUserRepository {
    private static final String TAG = "LoggedInUserRepository";
    private static final String DATA_SAVE_FILE_NAME = "LoggedInUser";
    private final WebService webService;
    private volatile User loggedInUser = null;
    private final Context context;

    public synchronized User getLoggedInUser() {
        if(loggedInUser==null){
            localRestore();
        }
        return loggedInUser;
    }

    private synchronized void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        localSave();
    }


    public LoggedInUserRepository(WebService webService, Context context) {
        this.context = context;
        this.webService = webService;
    }

    public int getLoggedInUserId() {
        return getLoggedInUser().getUserid();
    }

    public void setLoggedInUser(final ItemReceiver<User> itemReceiver, String credential) {
        webService.getUserByCredential(new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                if (user != null) {
                    setLoggedInUser(user);
                }
                itemReceiver.onItemArrived(user);
            }

            @Override
            public void onRequestFailure() {
                itemReceiver.onRequestFailure();
            }
        }, credential);
    }

    public void update(final ItemReceiver<Boolean> itemReceiver, User user) {
        webService.updateUser(new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                if (user != null) {
                    setLoggedInUser(user);
                }
                itemReceiver.onItemArrived(true);
            }

            @Override
            public void onRequestFailure() {
                itemReceiver.onRequestFailure();
            }
        }, user);

    }

    public void registerUser(final ItemReceiver<Boolean> itemReceiver, String mEmail) {
        ItemReceiver<User> userItemReceiver = new ItemReceiver<User>() {
            @Override
            public void onItemArrived(User user) {
                if (user == null)
                    throw new IllegalStateException();
                setLoggedInUser(user);
                itemReceiver.onItemArrived(true);
            }

            @Override
            public void onRequestFailure() {
                itemReceiver.onRequestFailure();
            }
        };
        webService.registerUser(userItemReceiver, mEmail);
    }

    private void localSave() {
        try {
            Log.d(TAG, "localSave: "+ loggedInUser);

            Assert.assertNotNull(context);
            FileOutputStream fos = context.openFileOutput(DATA_SAVE_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(loggedInUser);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void localRestore() {
        Log.d(TAG, "localRestore: restoring");

        try {
            FileInputStream fis = context.openFileInput(DATA_SAVE_FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            is.close();
            fis.close();
            loggedInUser =  user;
            Log.d(TAG, "localRestore: restored user = " + user);

        } catch (IOException | ClassNotFoundException ignored) {
        }
    }

    public void clearLoggedInUser() {
        setLoggedInUser(null);
    }
}
