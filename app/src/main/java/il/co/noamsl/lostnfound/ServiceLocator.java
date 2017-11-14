package il.co.noamsl.lostnfound;

import il.co.noamsl.lostnfound.serverInterface.NoamRepositoryExternal;
import il.co.noamsl.lostnfound.serverInterface.NoamServerInternal;
import il.co.noamsl.lostnfound.serverInterface.real.RepositoryExternalFactory;
import il.co.noamsl.lostnfound.serverInterface.real.ServerInternal;

/**
 * Created by noams on 13/11/2017.
 */

public class ServiceLocator {
    private static NoamServerInternal serverInternal=null;
    private static NoamRepositoryExternal serverExternal = null;

    public static NoamServerInternal getInternalServer(){
        if (serverInternal == null) {
            serverInternal = new ServerInternal();
        }
        return serverInternal;
    }

    public static NoamRepositoryExternal getExternalServer() {
        if(serverExternal==null){
            serverExternal = RepositoryExternalFactory.newInstance();
        }
        return serverExternal;
    }

}
