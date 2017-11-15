package il.co.noamsl.lostnfound;

import il.co.noamsl.lostnfound.repository.RepositoryExternal;
import il.co.noamsl.lostnfound.serverInterface.WebService;
import il.co.noamsl.lostnfound.serverInterface.RepositoryExternalFactory;
import il.co.noamsl.lostnfound.serverInterface.WebServiceImpl;

/**
 * Created by noams on 13/11/2017.
 */

public class ServiceLocator {
    private static WebService serverInternal=null;
    private static RepositoryExternal externalRepository = null;

    public static WebService getInternalServer(){
        if (serverInternal == null) {
            serverInternal = new WebServiceImpl();
        }
        return serverInternal;
    }

    public static RepositoryExternal getExternalRepository() {
        if(externalRepository ==null){
            externalRepository = RepositoryExternalFactory.newInstance();
        }
        return externalRepository;
    }

}
