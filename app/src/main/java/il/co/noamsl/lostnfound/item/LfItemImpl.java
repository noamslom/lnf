package il.co.noamsl.lostnfound.item;

import il.co.noamsl.lostnfound.eitan.FoundTable;

/**
 * Created by noams on 04/08/2017.
 */

public class LfItemImpl implements LfItem {
    private final Integer id;
    private String name;
    private String description;
    private String location;
    private Integer owner;
    private NoamImage picture;
    private boolean relevant;
    private boolean isAFound;

    public LfItemImpl(int id, String name, String description, String location, Integer owner, NoamImage picture, boolean isAFound) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.picture = picture;
        this.relevant = true;
        this.isAFound = isAFound;
    }



    public NoamImage getMainImage() {
        return picture;
    }

    public String getTitle() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean isAFound() {
        return isAFound;
    }

    @Override
    public boolean isALost() {
        return !isAFound;
    }

    @Override
    public FoundTable toFoundTable() {
        //// FIXME: 14/11/2017 change picture from string and dont put null
        return new FoundTable();

    }
}
