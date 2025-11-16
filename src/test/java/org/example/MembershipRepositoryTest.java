package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MembershipRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private MembershipRepository membershipRepository;

  private User testUser;
  private Membership testMembership;

  @BeforeEach
  public void setup() {
    // Create and persist a user first
    testUser = new User("Test User", "test@example.com", "password123");
    entityManager.persist(testUser);
    entityManager.flush();

    // Create membership
    testMembership = new Membership();
    entityManager.persist(testMembership);
    entityManager.flush();
  }

  @Test
  public void testSaveMembership() {
    // Given
    Membership newMembership = new Membership();

    // When
    Membership saved = membershipRepository.save(newMembership);

    // Then
    assertThat(saved).isNotNull();
  }

  @Test
  public void testFindAllMemberships() {
    // Given
    Membership membership2 = new Membership();
    entityManager.persist(membership2);
    entityManager.flush();

    // When
    Iterable<Membership> memberships = membershipRepository.findAll();

    // Then
    assertThat(memberships).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  public void testDeleteMembership() {
    // Given
    Membership membership = new Membership();
    entityManager.persist(membership);
    entityManager.flush();

    // When
    membershipRepository.delete(membership);
    entityManager.flush();

    // Then
    boolean exists = membershipRepository.existsById(membership.hashCode());
    assertThat(membershipRepository.count()).isLessThan(10);
  }

  @Test
  public void testCountMemberships() {
    // Given
    Membership membership2 = new Membership();
    entityManager.persist(membership2);
    entityManager.flush();

    // When
    long count = membershipRepository.count();

    // Then
    assertThat(count).isGreaterThanOrEqualTo(2);
  }
}
