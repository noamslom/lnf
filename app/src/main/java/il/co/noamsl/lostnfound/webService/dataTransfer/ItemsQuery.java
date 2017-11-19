package il.co.noamsl.lostnfound.webService.dataTransfer;


public class ItemsQuery implements Query{
    private final String name;
    private final String description;
    private final String location;
    private final Boolean isAFound;
    private final Integer owner;

    public Boolean getAFound() {
        return isAFound;
    }

    public Boolean isRelevant() {
        return isRelevant;
    }

    public void setRelevant(Boolean relevant) {
        isRelevant = relevant;
    }

    private Boolean isRelevant;

    public Integer getOwner() {
        return owner;
    }


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

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;
        return isAFound != null ? isAFound.equals(that.isAFound) : that.isAFound == null;

    }

    @Override
    public String toString() {
        return "ItemsQuery{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", isAFound=" + isAFound +
                ", owner=" + owner +
                ", isRelevant=" + isRelevant +
                '}';
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (isAFound != null ? isAFound.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (isRelevant != null ? isRelevant.hashCode() : 0);
        return result;
    }

    public ItemsQuery(String name, String description, String location, Boolean isAFound, Boolean isRelevant) {
        this(name, description, location, isAFound, null,isRelevant);
    }

    public ItemsQuery(String name, String description, String location, Boolean isAFound, Integer owner,Boolean isRelevant) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.isAFound = isAFound;
        this.owner = owner;
        this.isRelevant = isRelevant;
    }

    public Boolean isAFound() {
        return isAFound;
    }
}
