package il.co.noamsl.lostnfound.repository;

import android.content.Context;
import android.util.Log;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.webService.WebService;

/**
 * Created by noams on 13/11/2017.
 */

public class Repository {
    private static final String TAG = "Repository";
    private ItemsRepository itemsRepository;
    private UsersRepository userRepository;

    private LoggedInUserRepository loggedInUserRepository;
    private SettingsRepository settingsRepository;
    private WebService webService;
    private Context context;


    public Repository(Context applicationContext) {
        this.context = applicationContext;
        init();
    }

    private void init() {
        webService = new WebService();
        itemsRepository = new ItemsRepository(webService);
        loggedInUserRepository = new LoggedInUserRepository(webService, context);
        settingsRepository = new SettingsRepository();
        userRepository = new UsersRepository(webService);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void requestItems(Request<LfItem> request, RequestAgent requestAgent) {
        itemsRepository.requestItems(request, requestAgent);
    }

    public void addItem(final ItemReceiver<Boolean> itemReceiver, LfItem lfItem) {
        itemsRepository.addItem(itemReceiver, lfItem);
    }

    public LfItem getItemById(int itemId) {
        LfItem item = itemsRepository.getItemById(itemId);
        if (item == null) {
//            throw new IllegalStateException("Should not ask for an item not present");
            Log.e(TAG, "Should not ask for an item not present");
            return null;
        }
        return item;
    }

    public void getUserById(ItemReceiver<User> itemReceiver, int owner) {
        userRepository.getUserById(itemReceiver, owner);
    }


    public int getLoggedInUserId() {
        return loggedInUserRepository.getLoggedInUserId();
    }

    public void setLoggedInUserId(ItemReceiver<User> itemReceiver, String credential) {
        loggedInUserRepository.setLoggedInUser(itemReceiver, credential);
    }

    public void updateItem(final ItemReceiver<Boolean> itemReceiver, LfItem newItem) {
        itemsRepository.updateItem(itemReceiver, newItem);
    }

    public User getLoggedInUser() {
        return loggedInUserRepository.getLoggedInUser();
    }

    public void updateUser(final ItemReceiver<Boolean> itemReceiver,User user) {
        if (user.getUserid() == getLoggedInUserId()) {
            loggedInUserRepository.update(itemReceiver,user);
        } else {
            throw new IllegalStateException("only able to change logged in user");
        }
    }

    public void registerUser(ItemReceiver<Boolean> itemReceiver, String mEmail) {
        loggedInUserRepository.registerUser(itemReceiver, mEmail);
    }

    public void clearLoggedInUser() {
        loggedInUserRepository.clearLoggedInUser();
    }
}
