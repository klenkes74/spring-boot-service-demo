package com.example.demo.model;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@Slf4j
public class RandomChangeLogGenerator {
    private static final AtomicLong counter = new AtomicLong(0);
    private static final Faker faker = new Faker();

    public static ChangeLog generateRandomChangeLog() {
        return ChangeLog.builder()
                .withId(counter.addAndGet(1))
                .withUuid(UUID.randomUUID())
                .withTimestamp(LocalDateTime.now().minusMinutes(new Random().nextInt(1000)))
                .withMethod(ChangeLogAction.random())
                .withUser(faker.name().username())
                .withAttributes(randomAttributes("key1", "key2", "key3"))
                .build();
    }

    private static Map<String, String> randomAttributes(String... keys) {
        HashMap<String, String> result = new HashMap<>(keys.length);

        for (String key : keys) {
            result.put(key, faker.gameOfThrones().house());
        }

        return result;
    }
}
