package com.example.demo.service;

import com.example.demo.model.ChangeLog;
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

    private static final SortedSet<ChangeLog> changeLog = new ConcurrentSkipListSet<>(new ChangeLog());

    static {
        for (int i = 0; i < CHANGELOG_ENTRY_COUNT; i++) {
            changeLog.add(ChangeLog.random());
        }
    }

    public ChangelogService() {
        log.info("Working with changelog: {}", changeLog);
    }

    public List<ChangeLog> getLastChangeLogEntries(final int number) {
        return changeLog.stream().limit(number).collect(Collectors.toList());
    }

    public List<ChangeLog> getEarlierEntries(final LocalDateTime end) {
        return changeLog.stream().filter(d -> d.getTimestamp().isBefore(end)).collect(Collectors.toList());
    }
}
