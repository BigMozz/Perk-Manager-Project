package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int uid;
    private String Name;
    private String Email;
    private String Password;

    public User() {

    }

    public User(String name, String email, String password) {
        this.Name = name;
        this.Email = email;
        this.Password = password;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        this.Email = email;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        this.Password = password;
    }
}
