package org.example;
import jakarta.persistence.*;

@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;
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