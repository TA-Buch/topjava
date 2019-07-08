package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(4, "Нулевой", "000@mail.ru", "000", 1800, true, EnumSet.of(Role.ROLE_USER)),
            new User(1, "Первый", "111@mail.ru", "111", 2000, true, EnumSet.of(Role.ROLE_USER)),
            new User(2, "Второй", "222@mail.ru", "222", 1800, true, EnumSet.of(Role.ROLE_ADMIN)),
            new User(3, "Третий", "333@mail.ru", "333", 2000, true, EnumSet.of(Role.ROLE_USER))
    );

//    public static List<MealTo> getWithExcess(Collection<Meal> meals, int caloriesPerDay) {
//        return getFilteredWithExcess(meals, caloriesPerDay, meal -> true);
//    }
//
//    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
//        return getFilteredWithExcess(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
//    }
}
