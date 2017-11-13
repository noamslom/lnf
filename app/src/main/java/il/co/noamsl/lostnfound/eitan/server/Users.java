package il.co.noamsl.lostnfound.eitan.server;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByUserid", query = "SELECT u FROM Users u WHERE u.userid = :userid"),
    @NamedQuery(name = "Users.getMaxUserId", query = "SELECT MAX(u.userid) FROM Users u"),
    @NamedQuery(name = "Users.getIdByEmail", query = "SELECT u.userid FROM Users u WHERE u.email = :email")
})
public class Users implements Serializable {

    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 32)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Size(max = 255)
    @Column(name = "ADDRESS")
    private String address;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERID")
    private Integer userid;
    
    private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    public Users() {
    }

    public Users(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    
    public boolean validateEmail() {
        return EMAIL_REGEX.matcher(this.email).matches();
    }
    
    public void update(Users other) {
        if (other.name != null)
            this.name = other.name;
        if (other.address != null) 
            this.address = other.name;
        if (other.phoneNumber != null) 
            this.phoneNumber = other.phoneNumber;
        if (other.email != null) 
            this.email = other.email;
    }
}
