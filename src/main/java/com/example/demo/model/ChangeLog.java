package com.example.demo.model;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
@Data
@ToString
@EqualsAndHashCode
@Slf4j
public class ChangeLog implements Serializable, Comparator<ChangeLog> {
    private static final AtomicLong counter = new AtomicLong(0);
    private static final Faker faker = new Faker();

    private Long id;
    private UUID uuid;
    private LocalDateTime timestamp;
    private ChangeLogAction method;
    private String user;

    @Builder.Default
    private final Map<String, String> attributes = new HashMap<>();

    public static ChangeLog random() {
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

    @Override
    public int compare(ChangeLog changeLog, ChangeLog t1) {
        // sort reverse!
        return t1.getTimestamp().compareTo(changeLog.getTimestamp());
    }
}
