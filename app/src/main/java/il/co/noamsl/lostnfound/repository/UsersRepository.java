package il.co.noamsl.lostnfound.repository;

import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.cache.Cache;
import il.co.noamsl.lostnfound.webService.WebService;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

class UsersRepository{
    private WebService webService;

    public UsersRepository(WebService webService) {
        this.webService = webService;
    }

    public void getUserById(ItemReceiver<User> itemReceiver,int owner) {
        webService.getUserById(itemReceiver,owner);
    }

}
