package com.example.demo.service;

import com.example.demo.model.ChangeLog;
import com.example.demo.model.ChangeLogReverseComparator;
import com.example.demo.model.RandomChangeLogGenerator;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@Service
@Slf4j
public class ChangelogService {
    private static final long CHANGELOG_ENTRY_COUNT = 100L;

    private static final SortedSet<ChangeLog> changeLog = new ConcurrentSkipListSet<>(new ChangeLogReverseComparator());

    private final Counter lastCounter;
    private final Counter earlierCounter;



    static {
        for (int i = 0; i < CHANGELOG_ENTRY_COUNT; i++) {
            changeLog.add(RandomChangeLogGenerator.generateRandomChangeLog());
        }
    }

    public ChangelogService() {
        log.info("Working with changelog: {}", changeLog);

        lastCounter = Counter.builder("changelog_last_called")
                .description("How many times got the retrieve last <n> log entries got called?")
                .register(Metrics.globalRegistry);
        earlierCounter = Counter.builder("changelog_earlier_called")
                .description("How many times got the retrieve earlier-than log entries got called?")
                .register(Metrics.globalRegistry);
    }

    @Timed
    public List<ChangeLog> getLastChangeLogEntries(final int number) {
        List<ChangeLog> result = changeLog.stream().limit(number).collect(Collectors.toList());

        try {
            return result;
        } finally {
            lastCounter.increment();
            log.info("Returned changelogs: {}", result);
        }
    }

    @Timed
    public List<ChangeLog> getEarlierEntries(final LocalDateTime end) {
        List<ChangeLog> result = changeLog.stream().filter(d -> d.getTimestamp().isBefore(end)).collect(Collectors.toList());

        try {
            return result;
        } finally {
            earlierCounter.increment();
            log.info("Returned changelogs: {}", result);
        }
    }
}
