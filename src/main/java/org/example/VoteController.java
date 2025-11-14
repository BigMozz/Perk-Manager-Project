package org.example;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "*")

public class VoteController {

        private final VoteService voteService;
        private final UserRepository userRepository;
        private final PerkRepository perkRepository;

        public VoteController(VoteService voteService, UserRepository userRepository,
                              PerkRepository perkRepository) {
            this.voteService = voteService;
            this.userRepository = userRepository;
            this.perkRepository = perkRepository;
        }

        @PostMapping("/upvote")
        public String upvote(@RequestParam Integer userId, @RequestParam Integer perkId) {
            User user = userRepository.findById(userId).orElse(null);
            Perk perk = perkRepository.findPerkByPid(perkId);

            if (user == null || perk == null) {
                return "User or Perk not found";
            }

            voteService.addVote(user, perk, true);
            return "Upvote recorded";
        }

        @PostMapping("/downvote")
        public String downvote(@RequestParam Integer userId, @RequestParam Integer perkId) {
            User user = userRepository.findById(userId).orElse(null);
            Perk perk = perkRepository.findPerkByPid(perkId);

            if (user == null || perk == null) {
                return "User or Perk not found";
            }

            voteService.addVote(user, perk, false);
            return "Downvote recorded";
        }

        @GetMapping("/perk/{perkId}")
        public List<Rating> getVotesForPerk(@PathVariable Integer perkId) {
            Perk perk = perkRepository.findPerkByPid(perkId);
            return voteService.getVotesByPerk(perk);
        }

    @GetMapping("/user/{userId}")
    public List<Rating> getVotesForUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).get();
        return voteService.getVotesByUser(user);
    }










}
