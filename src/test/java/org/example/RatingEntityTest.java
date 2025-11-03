package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingEntityTest {

  private User testUser;
  private Perk testPerk;

  @BeforeEach
  public void setup() {
    testUser = new User("Test User", "test@example.com", "password");
    Membership membership = new Membership();
    testPerk = new Perk(
        "Test Perk",
        "10% off",
        "Electronics",
        LocalDate.now().plusDays(30),
        membership);
  }

  @Test
  public void testRatingCreation() {
    // When
    Rating rating = new Rating(testUser, testPerk, 5, 2);

    // Then
    assertThat(rating).isNotNull();
    assertThat(rating.getUser()).isEqualTo(testUser);
    assertThat(rating.getPerk()).isEqualTo(testPerk);
    assertThat(rating.getUpvote()).isEqualTo(5);
    assertThat(rating.getDownvote()).isEqualTo(2);
  }

  @Test
  public void testRatingSetters() {
    // Given
    Rating rating = new Rating();

    // When
    rating.setRid(1);
    rating.setUser(testUser);
    rating.setPerk(testPerk);
    rating.setUpvote(10);
    rating.setDownvote(3);

    // Then
    assertThat(rating.getRid()).isEqualTo(1L);
    assertThat(rating.getUser()).isEqualTo(testUser);
    assertThat(rating.getPerk()).isEqualTo(testPerk);
    assertThat(rating.getUpvote()).isEqualTo(10);
    assertThat(rating.getDownvote()).isEqualTo(3);
  }

  @Test
  public void testRatingDefaultConstructor() {
    // When
    Rating rating = new Rating();

    // Then
    assertThat(rating).isNotNull();
  }

  @Test
  public void testRatingVoteCalculation() {
    // Given
    Rating rating = new Rating(testUser, testPerk, 15, 5);

    // Then
    int netVotes = rating.getUpvote() - rating.getDownvote();
    assertThat(netVotes).isEqualTo(10);
  }

  @Test
  public void testRatingWithZeroVotes() {
    // When
    Rating rating = new Rating(testUser, testPerk, 0, 0);

    // Then
    assertThat(rating.getUpvote()).isEqualTo(0);
    assertThat(rating.getDownvote()).isEqualTo(0);
  }
}
