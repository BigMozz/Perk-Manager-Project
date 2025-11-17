package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerkManagerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHomePageLoads() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/",
                String.class
        );
        assertThat(response).isNotNull();
    }

    @Test
    public void testLoginPageAccessible() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/login",
                String.class
        );
        assertThat(response).isNotNull();
    }

    @Test
    public void testSignupPageAccessible() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/signup",
                String.class
        );
        assertThat(response).isNotNull();
    }

    @Test
    public void testGetAllPerks() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/perks",
                String.class
        );
        assertThat(response).isNotNull();
    }

    @Test
    public void testRatingApiAccessible() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/votes/user/1",
                String.class
        );
        assertThat(response).isNotNull();
    }
}
