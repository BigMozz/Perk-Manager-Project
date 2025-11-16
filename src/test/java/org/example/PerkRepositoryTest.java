package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PerkRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PerkRepository perkRepository;

  private User testUser;
  private Membership testMembership;
  private Perk testPerk;

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
        "Free Shipping",
        "10% off",
        "Electronics",
        LocalDate.now().plusDays(30),
        testMembership);
    entityManager.persist(testPerk);
    entityManager.flush();
  }

  @Test
  public void testSavePerk() {
    // Given
    Perk newPerk = new Perk(
        "Discount Voucher",
        "20% off",
        "Clothing",
        LocalDate.now().plusDays(60),
        testMembership);

    // When
    Perk saved = perkRepository.save(newPerk);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getPid()).isNotNull();
    assertThat(saved.getTitle()).isEqualTo("Discount Voucher");
    assertThat(saved.getDiscount()).isEqualTo("20% off");
    assertThat(saved.getProduct()).isEqualTo("Clothing");
  }

  @Test
  public void testFindPerkByPid_Success() {
    // When
    Perk found = perkRepository.findPerkByPid(testPerk.getPid());

    // Then
    assertThat(found).isNotNull();
    assertThat(found.getTitle()).isEqualTo("Free Shipping");
    assertThat(found.getDiscount()).isEqualTo("10% off");
  }

  @Test
  public void testFindPerkByPid_NotFound() {
    // When
    Perk found = perkRepository.findPerkByPid(99999);

    // Then
    assertThat(found).isNull();
  }

  @Test
  public void testUpdatePerk() {
    // Given
    testPerk.setTitle("Updated Free Shipping");
    testPerk.setDiscount("15% off");

    // When
    perkRepository.save(testPerk);
    entityManager.flush();
    Perk updated = perkRepository.findPerkByPid(testPerk.getPid());

    // Then
    assertThat(updated).isNotNull();
    assertThat(updated.getTitle()).isEqualTo("Updated Free Shipping");
    assertThat(updated.getDiscount()).isEqualTo("15% off");
  }

  @Test
  public void testDeletePerk() {
    // Given
    int perkId = testPerk.getPid();

    // When
    perkRepository.delete(testPerk);
    Perk found = perkRepository.findPerkByPid(perkId);

    // Then
    assertThat(found).isNull();
  }

  @Test
  public void testFindAllPerks() {
    // Given
    Perk perk2 = new Perk(
        "Gift Card",
        "5% off",
        "Food",
        LocalDate.now().plusDays(45),
        testMembership);
    entityManager.persist(perk2);
    entityManager.flush();

    // When
    Iterable<Perk> perks = perkRepository.findAll();

    // Then
    assertThat(perks).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  public void testPerkExpiryDate() {
    // When
    Perk found = perkRepository.findPerkByPid(testPerk.getPid());

    // Then
    assertThat(found.getExpiryDate()).isAfter(LocalDate.now());
  }

  @Test
  public void testFindById() {
    // When
    Perk found = perkRepository.findById(testPerk.getPid()).orElse(null);

    // Then
    assertThat(found).isNotNull();
    assertThat(found.getTitle()).isEqualTo("Free Shipping");
  }

  @Test
  public void testPerkExistsById() {
    // When
    boolean exists = perkRepository.existsById(testPerk.getPid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  public void testCountPerks() {
    // Given
    Perk perk2 = new Perk(
        "Bonus Points",
        "2x points",
        "General",
        LocalDate.now().plusDays(90),
        testMembership);
    entityManager.persist(perk2);
    entityManager.flush();

    // When
    long count = perkRepository.count();

    // Then
    assertThat(count).isEqualTo(2);
  }
}
