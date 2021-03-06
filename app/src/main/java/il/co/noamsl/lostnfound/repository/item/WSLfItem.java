package il.co.noamsl.lostnfound.repository.item;


public interface WSLfItem {
    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Integer getOwner();

    void setOwner(Integer owner);

    String getPicture();

    void setPicture(String picture);

    Integer getRecordid();

    void setRecordid(Integer recordid);

    String getLocation();

    void setLocation(String location);

    Boolean getRelevant();

    void setRelevant(Boolean relevant);

    String toString();

}
