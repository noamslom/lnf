package il.co.noamsl.lostnfound.webService.dataTransfer;

/**
 * Created by noams on 15/11/2017.
 */

public class ItemQuery {
    private final String name;
    private final String description;
    private final String location;
    private final boolean isAFound;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public ItemQuery(String name, String description, String location, boolean isAFound) {

        this.name = name;
        this.description = description;
        this.location = location;
        this.isAFound = isAFound;
    }

    public boolean isAFound() {
        return isAFound;
    }
}
