package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ + 1;
    public static final int BREAKFAST_ID = START_SEQ + 10;
    public static final int DINER_ID = START_SEQ + 11;

    public static final Meal MEAL_BREAKFAST = new Meal(BREAKFAST_ID,
            LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "завтрак", 500);
    public static final Meal MEAL_DINER = new Meal(DINER_ID,
            LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "обед", 1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
