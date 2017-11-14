package il.co.noamsl.lostnfound.item;

import il.co.noamsl.lostnfound.eitan.FoundTable;
import il.co.noamsl.lostnfound.eitan.LostTable;

/**
 * Created by noams on 14/11/2017.
 */

public interface LfItem {

    NoamImage getMainImage();

    String getName();

    Integer getId();

    boolean isAFound();

    boolean isALost();

    FoundTable toFoundTable();

    LostTable toLostTable();
}
