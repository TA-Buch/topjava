package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
            Map<LocalDate, Integer> dailyCalories = new HashMap<>();
            for (UserMeal meal : meals) {
                LocalDate date = meal.getDateTime().toLocalDate();
                dailyCalories.put(date, dailyCalories.getOrDefault(date, 0) + meal.getCalories());
            }

            List<UserMealWithExcess> result = new ArrayList<>();
            for (UserMeal meal : meals) {
                LocalTime mealTime = meal.getDateTime().toLocalTime();
                LocalDate mealDate = meal.getDateTime().toLocalDate();

                if (!mealTime.isBefore(startTime) && !mealTime.isAfter(endTime)) {
                    boolean excess = dailyCalories.get(mealDate) > caloriesPerDay;
                    result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
                }
            }

            return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)
                ));

        return meals.stream()
                .filter(meal -> {
                    LocalTime mealTime = meal.getDateTime().toLocalTime();
                    return !mealTime.isBefore(startTime) && !mealTime.isAfter(endTime);
                })
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        dailyCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay
                ))
                .collect(Collectors.toList());
    }
}
