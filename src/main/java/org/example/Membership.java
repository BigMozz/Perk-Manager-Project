package org.example;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Membership {
    @Id
    private Long mid;
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private String type;
    private int number;


    private Membership(User user, String type, int number) {
        this.user = user;
        this.type = type;
        this.number = number;


    }

    public Membership() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}