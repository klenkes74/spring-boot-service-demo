package com.example.demo.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
public enum ChangeLogAction {
    PUT,
    POST,
    DELETE;

    /** A small randomizer found on https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum */
    private static final List<ChangeLogAction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ChangeLogAction random() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
