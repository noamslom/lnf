package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.item.LFItem;
import il.co.noamsl.lostnfound.serverInterface.Request;
import il.co.noamsl.lostnfound.serverInterface.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.real.WebServiceImpl;

/**
 * Created by noams on 13/11/2017.
 */

public class RepositoryImpl implements Repository{
    private ItemsRepository itemsRepository;
    private LoggedInUserRepository loggedInUserRepository;
    private SettingsRepository settingsRepository;
    private WebServiceImpl serverExternal;
    private static RepositoryImpl globalRepository;

    public RepositoryImpl() {
        serverExternal = new WebServiceImpl();
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
    public void requestItems(Request<LFItem> request, RequestAgent requestAgent) {
        itemsRepository.requestItems(request, requestAgent);
    }

    @Override
    public FakeItem getItemById(long itemId) {
        return serverExternal.getItemById(itemId);
    }

    @Override
    public void addItem(String text) {
        serverExternal.addItem(text);
    }
}
