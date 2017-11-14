package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.serverInterface.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;
import il.co.noamsl.lostnfound.serverInterface.real.ServerInternal;

/**
 * Created by noams on 13/11/2017.
 */

public class RepositoryImpl implements Repository{
    private ItemsRepository itemsRepository;
    private LoggedInUserRepository loggedInUserRepository;
    private SettingsRepository settingsRepository;
    private ServerInternal serverInternal;
    private static RepositoryImpl globalRepository;

    public RepositoryImpl() {
        serverInternal = new ServerInternal();
        itemsRepository = new ItemsRepository();
        loggedInUserRepository = new LoggedInUserRepository();
        settingsRepository = new SettingsRepository();
    }

    public static RepositoryImpl getGlobal() {
        if (globalRepository == null) {
            globalRepository = new RepositoryImpl();
        }
        return globalRepository;
    }

    @Override
    public void requestItems(ItemReceiver itemsReceiver, RequestAgent requestAgent) {
        itemsRepository.requestItems(itemsReceiver, requestAgent);
    }

    @Override
    public FakeItem getItemById(long itemId) {
        return serverInternal.getItemById(itemId);
    }

    @Override
    public void addItem(String text) {
        serverInternal.addItem(text);
    }
}
