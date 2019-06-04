package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> a = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        a.forEach(e -> System.out.println(e.toString()));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> userMealOut = new ArrayList<>();
        HashMap<LocalDate, Integer> exceededMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                userMealOut.add(new UserMeal(dateTime, meal.getDescription(), meal.getCalories()));
            }
            exceededMap.merge(dateTime.toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        for (UserMeal aUserMealOut : userMealOut) {
            LocalDateTime dateTime = aUserMealOut.getDateTime();
            Integer calories = exceededMap.getOrDefault(dateTime.toLocalDate(), caloriesPerDay);
            if (calories > caloriesPerDay) {
                userMealWithExceeds.add(new UserMealWithExceed(dateTime, aUserMealOut.getDescription(), aUserMealOut.getCalories(), false));
            } else {
                userMealWithExceeds.add(new UserMealWithExceed(dateTime, aUserMealOut.getDescription(), aUserMealOut.getCalories(), true));
            }
        }
        return userMealWithExceeds;
    }
}
