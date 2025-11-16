package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User testUser;

  @BeforeEach
  public void setup() {
    testUser = new User("John Doe", "john.doe@example.com", "password123");
    entityManager.persist(testUser);
    entityManager.flush();
  }

  @Test
  public void testFindUserByEmail_Success() {
    // When
    User found = userRepository.findByEmail("john.doe@example.com");

    // Then
    assertThat(found).isNotNull();
    assertThat(found.getName()).isEqualTo("John Doe");
    assertThat(found.getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  public void testFindUserByEmail_NotFound() {
    // When
    User found = userRepository.findByEmail("nonexistent@example.com");

    // Then
    assertThat(found).isNull();
  }

  @Test
  public void testSaveUser() {
    // Given
    User newUser = new User("Jane Smith", "jane.smith@example.com", "password456");

    // When
    User savedUser = userRepository.save(newUser);

    // Then
    assertThat(savedUser.getUid()).isNotNull();
    assertThat(savedUser.getName()).isEqualTo("Jane Smith");
    assertThat(savedUser.getEmail()).isEqualTo("jane.smith@example.com");
  }

  @Test
  public void testDeleteUser() {
    // Given
    int userId = testUser.getUid();

    // When
    userRepository.delete(testUser);
    User found = userRepository.findById(userId).orElse(null);

    // Then
    assertThat(found).isNull();
  }

  @Test
  public void testUpdateUser() {
    // Given
    testUser.setName("John Updated");
    testUser.setEmail("john.updated@example.com");

    // When
    userRepository.save(testUser);
    entityManager.flush();
    User updated = userRepository.findByEmail("john.updated@example.com");

    // Then
    assertThat(updated).isNotNull();
    assertThat(updated.getName()).isEqualTo("John Updated");
  }

  @Test
  public void testFindAllUsers() {
    // Given
    User user2 = new User("Alice Brown", "alice.brown@example.com", "password789");
    entityManager.persist(user2);
    entityManager.flush();

    // When
    Iterable<User> users = userRepository.findAll();

    // Then
    assertThat(users).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  public void testFindById() {
    // When
    User found = userRepository.findById(testUser.getUid()).orElse(null);

    // Then
    assertThat(found).isNotNull();
    assertThat(found.getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  public void testUserExistsById() {
    // When
    boolean exists = userRepository.existsById(testUser.getUid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  public void testCountUsers() {
    // Given
    User user2 = new User("Bob Jones", "bob@example.com", "pass");
    entityManager.persist(user2);
    entityManager.flush();

    // When
    long count = userRepository.count();

    // Then
    assertThat(count).isEqualTo(2);
  }
}
