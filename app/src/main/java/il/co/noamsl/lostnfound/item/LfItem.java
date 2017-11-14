package il.co.noamsl.lostnfound.item;

import il.co.noamsl.lostnfound.eitan.FoundTable;

/**
 * Created by noams on 14/11/2017.
 */

public interface LfItem {

    NoamImage getMainImage();

    String getTitle();

    long getId();

    boolean isAFound();

    boolean isALost();

    FoundTable toFoundTable();
}
