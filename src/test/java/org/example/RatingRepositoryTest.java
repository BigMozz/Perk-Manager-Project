package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
    // Create user
    testUser = new User("Test User", "test@example.com", "password123");
    entityManager.persist(testUser);

    // Create membership
    testMembership = new Membership();
    entityManager.persist(testMembership);

    // Create perk
    testPerk = new Perk(
        "Test Perk",
        "10% off",
        "Electronics",
        LocalDate.now().plusDays(30),
        testMembership);
    entityManager.persist(testPerk);

    // Create rating with manually assigned ID
    testRating = new Rating(testUser, testPerk, 5, 2);
    testRating.setRid(1); // Manually set ID since no @GeneratedValue
    entityManager.persist(testRating);
    entityManager.flush();
  }

  @Test
  public void testSaveRating() {
    // Given
    Rating newRating = new Rating(testUser, testPerk, 10, 3);
    newRating.setRid(100); // Manually set unique ID

    // When
    Rating saved = ratingRepository.save(newRating);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUpvote()).isEqualTo(10);
    assertThat(saved.getDownvote()).isEqualTo(3);
  }

  @Test
  public void testFindRatingByRid_Success() {
    // When
    Rating found = ratingRepository.findByRid(1);

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

    ratingRepository.save(testRating);
    entityManager.flush();
    Rating updated = ratingRepository.findByRid(1);

    assertThat(updated).isNotNull();
    assertThat(updated.getUpvote()).isEqualTo(8);
    assertThat(updated.getDownvote()).isEqualTo(1);
  }

  @Test
  public void testDeleteRating() {
    ratingRepository.delete(testRating);
    Rating found = ratingRepository.findByRid(1);

    assertThat(found).isNull();
  }

  @Test
  public void testRatingUserRelationship() {
    Rating found = ratingRepository.findByRid(1);

    assertThat(found.getUser()).isNotNull();
    assertThat(found.getUser().getUid()).isEqualTo(testUser.getUid());
    assertThat(found.getUser().getName()).isEqualTo("Test User");
  }

  @Test
  public void testRatingPerkRelationship() {
    Rating found = ratingRepository.findByRid(1);

    assertThat(found.getPerk()).isNotNull();
    assertThat(found.getPerk().getPid()).isEqualTo(testPerk.getPid());
    assertThat(found.getPerk().getTitle()).isEqualTo("Test Perk");
  }

  @Test
  public void testFindAllRatings() {
    // Given
    Rating rating2 = new Rating(testUser, testPerk, 3, 1);
    rating2.setRid(2);
    entityManager.persist(rating2);
    entityManager.flush();

    // When
    Iterable<Rating> ratings = ratingRepository.findAll();

    // Then
    assertThat(ratings).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  public void testFindById() {
    // When
    Rating found = ratingRepository.findById(1).orElse(null);

    // Then
    assertThat(found).isNotNull();
    assertThat(found.getUpvote()).isEqualTo(5);
  }

  @Test
  public void testRatingExistsById() {
    // When
    boolean exists = ratingRepository.existsById(1);

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  public void testCountRatings() {
    // Given
    Rating rating2 = new Rating(testUser, testPerk, 7, 4);
    rating2.setRid(2);
    entityManager.persist(rating2);
    entityManager.flush();

    // When
    long count = ratingRepository.count();

    // Then
    assertThat(count).isEqualTo(2);
  }

  @Test
  public void testRatingWithZeroVotes() {
    // Given
    Rating zeroRating = new Rating(testUser, testPerk, 0, 0);
    zeroRating.setRid(3);

    // When
    Rating saved = ratingRepository.save(zeroRating);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUpvote()).isEqualTo(0);
    assertThat(saved.getDownvote()).isEqualTo(0);
  }

  @Test
  public void testMultipleRatingsForSamePerk() {
    // Given
    User user2 = new User("User 2", "user2@example.com", "pass");
    entityManager.persist(user2);

    Rating rating2 = new Rating(user2, testPerk, 8, 1);
    rating2.setRid(4);
    entityManager.persist(rating2);
    entityManager.flush();

    // When
    Iterable<Rating> allRatings = ratingRepository.findAll();

    // Then
    assertThat(allRatings).hasSizeGreaterThanOrEqualTo(2);
  }
}
