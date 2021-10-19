package com.example.demo.service;

import com.example.demo.model.ChangeLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@Slf4j
public class ChangeLogServiceTest {

    private ChangelogService sut;

    @Test
    public void shouldReturnFiveChangeLogEntries() {
        List<ChangeLog> result = sut.getLastChangeLogEntries(5);
        log.info("result: {}", result);

        Assertions.assertThat(result.size()).isEqualTo(5);
    }


    @Test
    public void shouldReturnARandumNumberOfLogEntries() {
        List<ChangeLog> result = sut.getEarlierEntries(LocalDateTime.now());
        log.info("result: {}", result);

        result.stream().forEach(e ->
        System.out.println(String.format("%d %s %s %s", e.getId(), e.getTimestamp().toString(), e.getMethod(), e.getUser())));
    }

    @BeforeEach
    public void setUp() {
        sut = new ChangelogService();
    }
}
