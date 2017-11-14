package il.co.noamsl.lostnfound;

import il.co.noamsl.lostnfound.serverInterface.RepositoryExternal;
import il.co.noamsl.lostnfound.serverInterface.WebService;
import il.co.noamsl.lostnfound.serverInterface.real.RepositoryExternalFactory;
import il.co.noamsl.lostnfound.serverInterface.real.WebServiceImpl;

/**
 * Created by noams on 13/11/2017.
 */

public class ServiceLocator {
    private static WebService serverInternal=null;
    private static RepositoryExternal serverExternal = null;

    public static WebService getInternalServer(){
        if (serverInternal == null) {
            serverInternal = new WebServiceImpl();
        }
        return serverInternal;
    }

    public static RepositoryExternal getExternalServer() {
        if(serverExternal==null){
            serverExternal = RepositoryExternalFactory.newInstance();
        }
        return serverExternal;
    }

}
