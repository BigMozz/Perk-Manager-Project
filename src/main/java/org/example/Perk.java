package org.example;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Perk {
    @Id
    @GeneratedValue
    private int pid;
    private String title;
    private String discount;
    private String product;
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "mid")
    private Membership membership;

    @OneToMany(mappedBy = "pid")
    private List<Rating> ratings;

    public Perk() {}

    public Perk(String title, String discount, String product, LocalDate expiryDate, Membership membership)
    {
        this.title = title;
        this.discount = discount;
        this.product = product;
        this.expiryDate = expiryDate;
        this.membership = membership;
    }

    public int getPid() { return pid; }
    public void setPid(int pid) { this.pid = pid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Membership getMembership() { return membership; }
    public void setMembership(Membership membership) { this.membership = membership; }

    public List<Rating> getRatings() { return ratings; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

}
