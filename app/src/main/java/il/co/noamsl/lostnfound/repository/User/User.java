package il.co.noamsl.lostnfound.repository.User;

import java.io.Serializable;

import il.co.noamsl.lostnfound.repository.cache.Cacheable;
import il.co.noamsl.lostnfound.webService.eitan.Users;


public class User implements Cacheable,Serializable {
    private Users wsUser;

    public User(Users wsUser) {
        this.wsUser = wsUser;
    }

    public String getName() {
        return wsUser.getName();
    }

    public void setName(String name) {
        wsUser.setName(name);
    }

    public String getEmail() {
        return wsUser.getEmail();
    }

    public void setEmail(String email) {
        wsUser.setEmail(email);
    }

    public String getPhoneNumber() {
        return wsUser.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        wsUser.setPhoneNumber(phoneNumber);
    }

    public String getAddress() {
        return wsUser.getAddress();
    }

    public void setAddress(String address) {
        wsUser.setAddress(address);
    }

    public Integer getUserid() {
        return wsUser.getUserid();
    }

    public void setUserid(Integer userid) {
        wsUser.setUserid(userid);
    }

    public boolean validateEmail() {
        return wsUser.validateEmail();
    }

    @Override
    public String toString() {
        return wsUser.toString();
    }

    @Override
    public String getCacheId() {
        return String.valueOf(getUserid());
    }

    public Users toWSUser() {
        return wsUser;
    }
}
