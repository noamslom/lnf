package il.co.noamsl.lostnfound.serverInterface;

import il.co.noamsl.lostnfound.item.Item;

/**
 * Created by noams on 05/11/2017.
 */

public interface ItemReceiver {
    void onItemArrived(Item item);
}
