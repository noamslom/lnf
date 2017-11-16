package il.co.noamsl.lostnfound.webService.dataTransfer;

/**
 * Created by noams on 15/11/2017.
 */

public class ItemsQuery {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsQuery that = (ItemsQuery) o;

        if (isAFound != that.isAFound) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return location != null ? location.equals(that.location) : that.location == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (isAFound ? 1 : 0);
        return result;
    }

    public ItemsQuery(String name, String description, String location, boolean isAFound) {

        this.name = name;
        this.description = description;
        this.location = location;
        this.isAFound = isAFound;
    }

    public boolean isAFound() {
        return isAFound;
    }
}
