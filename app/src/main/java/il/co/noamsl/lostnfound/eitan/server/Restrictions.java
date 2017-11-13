package il.co.noamsl.lostnfound.eitan.server;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "RESTRICTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Restrictions.findByRecordid", query = "SELECT r FROM Restrictions r WHERE r.recordid = :recordid")
})
public class Restrictions implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECORDID")
    private Integer recordid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private Boolean name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMAIL")
    private Boolean email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PHONE_NUMBER")
    private Boolean phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDRESS")
    private Boolean address;

    public Restrictions() {
    }

    public Restrictions(Integer recordid) {
        this.recordid = recordid;
    }

    public Restrictions(Integer recordid, Boolean name, Boolean email, Boolean phoneNumber, Boolean address) {
        this.recordid = recordid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public Boolean getName() {
        return name;
    }

    public void setName(Boolean name) {
        this.name = name;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Boolean phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAddress() {
        return address;
    }

    public void setAddress(Boolean address) {
        this.address = address;
    }
    
    public void update(Restrictions other) {
        if (other.name != null) 
            this.name = other.name;
        if (other.address != null) 
            this.address = other.address;
        if (other.email != null) 
            this.email = other.email;
        if (other.phoneNumber != null) 
            this.phoneNumber = other.phoneNumber;
    }
    
    public void setDefault() {
        if (this.name == null) 
            this.name = false;
        if (this.address == null) 
            this.address = false;
        if (this.email == null) 
            this.email = false;
        if (this.phoneNumber == null) 
            this.phoneNumber = false;
    }
}
