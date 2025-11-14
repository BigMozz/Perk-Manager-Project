package org.example;

import jakarta.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Perk perk;

    private int upvote;
    private int downvote;

    public Rating() {}

    public Rating(User user, Perk perk, int upvote,int downvote) {
        this.user = user;
        this.perk = perk;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    public long getRid() { return rid; }
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Perk getPerk() { return perk; }
    public void setPerk(Perk perk) { this.perk = perk; }

    public int getUpvote() { return upvote; }
    public void setUpvote(int upvote) { this.upvote = upvote; }
    public int getDownvote() { return downvote; }
    public void setDownvote(int downvote) { this.downvote = downvote; }

}
