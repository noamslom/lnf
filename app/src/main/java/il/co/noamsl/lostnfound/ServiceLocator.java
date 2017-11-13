package il.co.noamsl.lostnfound;

import il.co.noamsl.lostnfound.serverInterface.NoamServerExternal;
import il.co.noamsl.lostnfound.serverInterface.NoamServerInternal;
import il.co.noamsl.lostnfound.serverInterface.real.ServerExternalFactory;
import il.co.noamsl.lostnfound.serverInterface.real.ServerInternal;

/**
 * Created by noams on 13/11/2017.
 */

public class ServiceLocator {
    private static NoamServerInternal serverInternal=null;
    private static NoamServerExternal serverExternal = null;

    public static NoamServerInternal getInternalServer(){
        if (serverInternal == null) {
            serverInternal = new ServerInternal();
        }
        return serverInternal;
    }

    public static NoamServerExternal getExternalServer() {
        if(serverExternal==null){
            serverExternal = ServerExternalFactory.newInstance();
        }
        return serverExternal;
    }

}
