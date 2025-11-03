package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

  @Test
  public void testUserCreation() {
    // Given & When
    User user = new User("John Doe", "john.doe@example.com", "password123");

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getName()).isEqualTo("John Doe");
    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    assertThat(user.getPassword()).isEqualTo("password123");
  }

  @Test
  public void testUserSetters() {
    // Given
    User user = new User();

    // When
    user.setUid(1);
    user.setName("Jane Smith");
    user.setEmail("jane.smith@example.com");
    user.setPassword("newpassword");

    // Then
    assertThat(user.getUid()).isEqualTo(1);
    assertThat(user.getName()).isEqualTo("Jane Smith");
    assertThat(user.getEmail()).isEqualTo("jane.smith@example.com");
    assertThat(user.getPassword()).isEqualTo("newpassword");
  }

  @Test
  public void testUserDefaultConstructor() {
    // When
    User user = new User();

    // Then
    assertThat(user).isNotNull();
  }

  @Test
  public void testUserEmailValidFormat() {
    // Given
    User user = new User("Test User", "test@example.com", "password");

    // Then
    assertThat(user.getEmail()).contains("@");
    assertThat(user.getEmail()).contains(".");
  }
}
