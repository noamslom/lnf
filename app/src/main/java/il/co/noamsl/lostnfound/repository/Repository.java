package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.Request;
import il.co.noamsl.lostnfound.webService.dataTransfer.RequestAgent;
import il.co.noamsl.lostnfound.webService.WebService;

/**
 * Created by noams on 13/11/2017.
 */

public class Repository {
    private ItemsRepository itemsRepository;
    private LoggedInUserRepository loggedInUserRepository;
    private SettingsRepository settingsRepository;
    private WebService webService;
    private static Repository globalRepository;

    public Repository() {
        webService = new WebService();
        itemsRepository = new ItemsRepository(webService);
        loggedInUserRepository = new LoggedInUserRepository();
        settingsRepository = new SettingsRepository();
    }

    public static Repository getGlobal() {
        if (globalRepository == null) {
            globalRepository = new Repository();
        }
        return globalRepository;
    }

    public void requestItems(Request<LfItem> request, RequestAgent requestAgent) {
        itemsRepository.requestItems(request, requestAgent);
    }

    public void addItem(LfItem lfItem) {
        webService.addItem(lfItem);
    }
}
