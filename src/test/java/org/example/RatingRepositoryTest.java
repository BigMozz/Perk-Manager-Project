package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.defer-datasource-initialization=true"
})
public class RatingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RatingRepository ratingRepository;

    private User testUser;
    private Membership testMembership;
    private Perk testPerk;
    private Rating testRating;

    @BeforeEach
    public void setup() {
        // Clear persistence context
        entityManager.clear();

        // Create and persist user
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = entityManager.persistAndFlush(testUser);

        // Create and persist membership
        testMembership = new Membership();
        testMembership.setUser(testUser);
        testMembership.setType("Premium");
        testMembership.setNumber(12345);
        testMembership = entityManager.persistAndFlush(testMembership);

        // Create and persist perk
        testPerk = new Perk();
        testPerk.setTitle("Test Perk");
        testPerk.setDiscount("10% off");
        testPerk.setProduct("Electronics");
        testPerk.setExpiryDate(LocalDate.now().plusDays(30));
        testPerk.setMembership(testMembership);
        testPerk = entityManager.persistAndFlush(testPerk);

        // Create and persist rating
        testRating = new Rating();
        testRating.setUser(testUser);
        testRating.setPerk(testPerk);
        testRating.setUpvote(5);
        testRating.setDownvote(2);
        testRating = entityManager.persistAndFlush(testRating);
    }

    @Test
    public void testSaveRating() {
        // Given
        Rating newRating = new Rating();
        newRating.setUser(testUser);
        newRating.setPerk(testPerk);
        newRating.setUpvote(10);
        newRating.setDownvote(3);

        // When
        Rating saved = ratingRepository.save(newRating);
        entityManager.flush();

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getRid()).isNotNull();
        assertThat(saved.getUpvote()).isEqualTo(10);
        assertThat(saved.getDownvote()).isEqualTo(3);
    }

    @Test
    public void testFindRatingByRid_Success() {
        // When
        Rating found = ratingRepository.findByRid((int) testRating.getRid());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUpvote()).isEqualTo(5);
        assertThat(found.getDownvote()).isEqualTo(2);
    }

    @Test
    public void testFindRatingByRid_NotFound() {
        // When
        Rating found = ratingRepository.findByRid(99999);

        // Then
        assertThat(found).isNull();
    }

    @Test
    public void testUpdateRating() {
        // Given
        testRating.setUpvote(8);
        testRating.setDownvote(1);

        // When
        Rating updated = ratingRepository.save(testRating);
        entityManager.flush();
        entityManager.clear();

        Rating found = ratingRepository.findByRid((int) testRating.getRid());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUpvote()).isEqualTo(8);
        assertThat(found.getDownvote()).isEqualTo(1);
    }

    @Test
    public void testDeleteRating() {
        // When
        ratingRepository.delete(testRating);
        entityManager.flush();

        Rating found = ratingRepository.findByRid((int) testRating.getRid());

        // Then
        assertThat(found).isNull();
    }

    @Test
    public void testRatingUserRelationship() {
        // When
        Rating found = ratingRepository.findByRid((int) testRating.getRid());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUser()).isNotNull();
        assertThat(found.getUser().getUid()).isEqualTo(testUser.getUid());
        assertThat(found.getUser().getName()).isEqualTo("Test User");
    }

    @Test
    public void testRatingPerkRelationship() {
        // When
        Rating found = ratingRepository.findByRid((int) testRating.getRid());

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getPerk()).isNotNull();
        assertThat(found.getPerk().getPid()).isEqualTo(testPerk.getPid());
        assertThat(found.getPerk().getTitle()).isEqualTo("Test Perk");
    }

    @Test
    public void testFindAllRatings() {
        // Given
        Rating rating2 = new Rating();
        rating2.setUser(testUser);
        rating2.setPerk(testPerk);
        rating2.setUpvote(3);
        rating2.setDownvote(1);
        entityManager.persistAndFlush(rating2);

        // When
        Iterable<Rating> ratings = ratingRepository.findAll();

        // Then
        assertThat(ratings).isNotNull();
        int count = 0;
        for (Rating rating : ratings) {
            count++;
        }
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindById() {
        // When
        Rating found = ratingRepository.findById((int) testRating.getRid()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getUpvote()).isEqualTo(5);
    }

    @Test
    public void testRatingExistsById() {
        // When
        boolean exists = ratingRepository.existsById((int) testRating.getRid());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testCountRatings() {
        // Given
        Rating rating2 = new Rating();
        rating2.setUser(testUser);
        rating2.setPerk(testPerk);
        rating2.setUpvote(7);
        rating2.setDownvote(4);
        entityManager.persistAndFlush(rating2);

        // When
        long count = ratingRepository.count();

        // Then
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testRatingWithZeroVotes() {
        // Given
        Rating zeroRating = new Rating();
        zeroRating.setUser(testUser);
        zeroRating.setPerk(testPerk);
        zeroRating.setUpvote(0);
        zeroRating.setDownvote(0);

        // When
        Rating saved = ratingRepository.save(zeroRating);
        entityManager.flush();

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getUpvote()).isEqualTo(0);
        assertThat(saved.getDownvote()).isEqualTo(0);
    }

    @Test
    public void testMultipleRatingsForSamePerk() {
        // Given
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass");
        user2 = entityManager.persistAndFlush(user2);

        Rating rating2 = new Rating();
        rating2.setUser(user2);
        rating2.setPerk(testPerk);
        rating2.setUpvote(8);
        rating2.setDownvote(1);
        entityManager.persistAndFlush(rating2);

        // When
        Iterable<Rating> allRatings = ratingRepository.findAll();

        // Then
        int count = 0;
        for (Rating rating : allRatings) {
            count++;
        }
        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}
