package il.co.noamsl.lostnfound.item;

/**
 * Created by noams on 04/08/2017.
 */

public class Item {
    private final long ID;
    private String name;
    private String description;
    private NoamPlace place;
    private NoamRegisteredUser Owner;
    private NoamImage picture;
    private boolean relevant;

    public Item(long ID, String name, String description, NoamPlace place, NoamRegisteredUser owner, NoamImage picture) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.place = place;
        Owner = owner;
        this.picture = picture;
        this.relevant = true;
    }



    public NoamImage getMainImage() {
        return picture;
    }

    public String getTitle() {
        return name;
    }

    public long getId() {
        return ID;
    }
}
