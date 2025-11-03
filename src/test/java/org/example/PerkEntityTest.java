package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PerkEntityTest {

  private Membership testMembership;

  @BeforeEach
  public void setup() {
    testMembership = new Membership();
  }

  @Test
  public void testPerkCreation() {
    // Given
    LocalDate expiryDate = LocalDate.now().plusDays(30);

    // When
    Perk perk = new Perk(
        "Free Shipping",
        "10% off",
        "Electronics",
        expiryDate,
        testMembership);

    // Then
    assertThat(perk).isNotNull();
    assertThat(perk.getTitle()).isEqualTo("Free Shipping");
    assertThat(perk.getDiscount()).isEqualTo("10% off");
    assertThat(perk.getProduct()).isEqualTo("Electronics");
    assertThat(perk.getExpiryDate()).isEqualTo(expiryDate);
    assertThat(perk.getMembership()).isEqualTo(testMembership);
  }

  @Test
  public void testPerkSetters() {
    // Given
    Perk perk = new Perk();
    LocalDate newDate = LocalDate.now().plusDays(60);

    // When
    perk.setPid(1);
    perk.setTitle("Updated Title");
    perk.setDiscount("20% off");
    perk.setProduct("Clothing");
    perk.setExpiryDate(newDate);
    perk.setMembership(testMembership);

    // Then
    assertThat(perk.getPid()).isEqualTo(1);
    assertThat(perk.getTitle()).isEqualTo("Updated Title");
    assertThat(perk.getDiscount()).isEqualTo("20% off");
    assertThat(perk.getProduct()).isEqualTo("Clothing");
    assertThat(perk.getExpiryDate()).isEqualTo(newDate);
    assertThat(perk.getMembership()).isEqualTo(testMembership);
  }

  @Test
  public void testPerkDefaultConstructor() {
    // When
    Perk perk = new Perk();

    // Then
    assertThat(perk).isNotNull();
  }

  @Test
  public void testPerkExpiryDateInFuture() {
    // Given
    LocalDate futureDate = LocalDate.now().plusMonths(3);
    Perk perk = new Perk(
        "Future Discount",
        "15% off",
        "Books",
        futureDate,
        testMembership);

    // Then
    assertThat(perk.getExpiryDate()).isAfter(LocalDate.now());
  }
}
