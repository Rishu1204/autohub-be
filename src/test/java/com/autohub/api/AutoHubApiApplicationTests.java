package com.autohub.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.mail.username=", "spring.mail.password=", "app.booking.to-email="})
class AutoHubApiApplicationTests {
    @Test void contextLoads() {}
}
