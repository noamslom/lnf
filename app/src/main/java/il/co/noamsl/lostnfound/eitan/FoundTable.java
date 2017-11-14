package il.co.noamsl.lostnfound.eitan;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class FoundTable implements Serializable {

    @Element(required = false)
    private String name;

    @Element(required = false)
    private String description;

    @Element(required = false)
    private String location;

    @Element(required = false)
    private Integer owner;

    @Element(required = false)
    private String picture;

    @Element(required = false)
    private Integer recordid;

    public FoundTable(String name, String description, String location, Integer owner, String picture, Integer recordid, Boolean relevant) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.owner = owner;
        this.picture = picture;
        this.recordid = recordid;
        this.relevant = relevant;
    }

    @Element(required = false)
    private volatile Boolean relevant;

    public FoundTable() {
    }

    public FoundTable(Integer recordid) {
        this.recordid = recordid;
    }

    public FoundTable(Integer recordid, int owner) {
        this.recordid = recordid;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRelevant() {
        return relevant;
    }

    public void setRelevant(Boolean relevant) {
        this.relevant = relevant;
    }

    public String toString() {
        String s = "Found";
        if (this.name != null) s += ", name " + this.name;
        if (this.description != null) s += ", description " + this.description;
        if (this.location != null) s += ", location " + this.location;
        if (this.owner != null) s += ", owner " + this.owner;
        if (this.picture != null) s += ", picture " + this.picture;
        if (this.recordid != null) s += ", recordid " + this.recordid;
        if (this.relevant != null) s += ", relevant " + this.relevant;
        return s;
    }
}
