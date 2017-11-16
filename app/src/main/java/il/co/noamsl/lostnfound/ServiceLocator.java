package il.co.noamsl.lostnfound;

import il.co.noamsl.lostnfound.repository.external.RepositoryExternal;

/**
 * Created by noams on 13/11/2017.
 */

public class ServiceLocator {
//    private static WebService serverInternal=null;
    private static RepositoryExternal externalRepository = null;

/*
    public static WebService getInternalServer(){
        if (serverInternal == null) {
            serverInternal = new WebService();
        }
        return serverInternal;
    }
*/

    public static RepositoryExternal getExternalRepository() {
        if(externalRepository ==null){
            externalRepository = new RepositoryExternal();
        }
        return externalRepository;
    }

}
