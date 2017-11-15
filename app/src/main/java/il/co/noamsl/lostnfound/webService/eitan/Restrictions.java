package il.co.noamsl.lostnfound.webService.eitan;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class Restrictions implements Serializable {

    @Element(required = false)
    private Integer recordid;
    @Element(required = false)
    private Boolean name;
    @Element(required = false)
    private Boolean email;
    @Element(required = false)
    private Boolean phoneNumber;
    @Element(required = false)
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
