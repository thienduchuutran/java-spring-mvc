package vn.hoidanit.laptopshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id // we need this id bean for this class to become an entity without error
    @GeneratedValue(strategy = GenerationType.IDENTITY) // this will help id auto increments
    private long id;

    private String email;
    private String password;
    private String fullName;
    private String address;
    private String phone;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password= " + password + ", full name=" + fullName
                + ", address= " + address + ", phone=" + phone + " ]";
    }
}
