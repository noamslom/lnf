package il.co.noamsl.lostnfound.serverInterface.fake;

import il.co.noamsl.lostnfound.item.FakeItem;
import il.co.noamsl.lostnfound.item.RequestAgent;
import il.co.noamsl.lostnfound.serverInterface.ItemReceiver;
import il.co.noamsl.lostnfound.serverInterface.NoamServerInternal;

/**
 * Created by noams on 05/11/2017.
 */

public class FakeServerInternal implements NoamServerInternal {
    private final long PULL_DELAY = 100; //ms
    private static NoamServerInternal server = null;
    private final ItemsDB itemsDB;

    public FakeServerInternal() {

        itemsDB = new ItemsDB();
    }

    public static NoamServerInternal getGlobalFakeServer() {
        if (server == null) {
            server = new FakeServerInternal();
        }

        return server;
    }

    @Override
    public synchronized void requestItems(final ItemReceiver itemsReceiver, final RequestAgent requestAgent) {
        // a potentially  time consuming task
        while (requestAgent.getRequestedLeft() > 0) {
            FakeItem nextItem = itemsDB.getItem(requestAgent.getNextRequestSerial());
            if (nextItem == null)
                break;
            itemsReceiver.onItemArrived(nextItem);
            try {
                Thread.sleep(PULL_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public FakeItem getItemById(long itemId) {
        return itemsDB.getItemById(itemId);
    }

    @Override
    public void addItem(String text) {
        itemsDB.addItem(text);
    }
}
