package il.co.noamsl.lostnfound.webService.eitan;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.regex.Pattern;


@Root(strict = false)
public class Users implements Serializable {

    @Element(required = false)
    private String name;
    @Element(required = false)
    private String email;
    @Element(required = false)
    private String phoneNumber;
    @Element(required = false)
    private String address;
    @Element(required = false)
    private Integer userid;

    private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    public Users() {
    }

    public Users(Integer userid) {
        this.userid = userid;
    }

    public Users(String name, String email, String phoneNumber, String address, Integer userid) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (address != null) this.address = address;
        if (userid != null) this.userid = userid;
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

    public static boolean validateEmail(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    public String toString() {
        String s = "User";
        if (this.name != null) s += ", name " + this.name;
        if (this.email != null) s += ", email " + this.email;
        if (this.phoneNumber != null) s += ", phoneNumber " + this.phoneNumber;
        if (this.address != null) s += ", address " + this.address;
        if (this.userid != null) s += ", userid " + this.userid;
        return s;
    }
}
