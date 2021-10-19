package com.example.demo.model;

import java.util.Comparator;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-19
 * @since 1.0.0 2021-10-19
 */
public class ChangeLogReverseComparator implements Comparator<ChangeLog> {
    @Override
    public int compare(ChangeLog changeLog, ChangeLog t1) {
        // sort reverse!
        return t1.getTimestamp().compareTo(changeLog.getTimestamp());
    }
}
