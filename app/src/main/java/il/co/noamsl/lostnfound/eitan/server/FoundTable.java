package il.co.noamsl.lostnfound.eitan.server;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "FOUND_TABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FoundTable.findByOwner", query = "SELECT f FROM FoundTable f WHERE f.owner = :owner"),
    @NamedQuery(name = "FoundTable.findByRecordid", query = "SELECT f FROM FoundTable f WHERE f.recordid = :recordid"),
    @NamedQuery(name = "FoundTable.getMaxRecordId", query = "SELECT MAX(f.recordid) FROM FoundTable f")
})
public class FoundTable implements Serializable {

    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OWNER")
    private Integer owner;
    @Lob
    @Column(name = "PICTURE")
    private String picture;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECORDID")
    private Integer recordid;
    @Size(max = 255)
    @Column(name = "LOCATION")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RELEVANT")
    private Boolean relevant;

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
    
    public void update(FoundTable other) {
        if (other.name != null)
            this.name = other.name;
        if (other.description != null) 
            this.description = other.description;
        if (other.location != null)
            this.location = other.location;
        if (other.picture != null) 
            this.picture = other.picture;
        if (other.relevant != null) 
            this.relevant = other.relevant;
    }    
}
