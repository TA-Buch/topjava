package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL1_ID = 100003; // Начинаем после ID пользователей
    public static final int MEAL2_ID = 100004;

    public static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2024, 2, 21, 12, 30), "Lunch - Chicken and Rice", 600);
    public static final Meal MEAL2 = new Meal(MEAL2_ID, LocalDateTime.of(2024, 2, 21, 8, 0), "Breakfast - Oatmeal and Fruits", 400);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, 2, 22, 18, 0), "Dinner - Steak and Vegetables", 700);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Updated Lunch - Grilled Chicken with Salad");
        updated.setCalories(550);
        updated.setDateTime(LocalDateTime.of(2024, 2, 21, 13, 0));
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").isEqualTo(expected);
    }
}