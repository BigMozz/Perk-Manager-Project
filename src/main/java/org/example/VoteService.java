package org.example;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Rating addVote(User user, Perk perk, boolean isUpvote) {
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setPerk(perk);

        if (isUpvote) {
            rating.setUpvote(1);
            rating.setDownvote(0);
        } else {
            rating.setUpvote(0);
            rating.setDownvote(1);
        }

        return voteRepository.save(rating);
    }

    public List<Rating> getVotesByPerk(Perk perk) {
        return voteRepository.findByPerk(perk);
    }

    public List<Rating> getVotesByUser(User user) {
        return voteRepository.findByUser(user);
    }
}