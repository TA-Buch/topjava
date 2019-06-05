package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
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
        Map<LocalDate, Integer> exceededMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            exceededMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        for (UserMeal meal : mealList) {
            LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                boolean exceed = exceededMap.get(dateTime.toLocalDate()) > caloriesPerDay;
                userMealWithExceeds.add(new UserMealWithExceed(dateTime, meal.getDescription(), meal.getCalories(), exceed));
            }
        }
        return userMealWithExceeds;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                (e -> e.getDateTime().toLocalDate()),
                                Collectors.toList()),
                        map -> map.entrySet().stream()))
                .map(x -> x.getValue().stream()
                        .filter(e -> TimeUtil.isBetween(e.getDateTime().toLocalTime(),startTime, endTime))
                        .map(t -> new UserMealWithExceed(t.getDateTime(), t.getDescription(), t.getCalories(),
                                x.getValue().stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay))
                        .collect(Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::unmodifiableList))
                )
                .collect(Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::addAll));
//                        .forEach(t -> new UserMealWithExceed(t.getDateTime(), t.getDescription(), t.getCalories(),
//                                x.getValue().stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay))

//                .collect(Collectors.toList())
//                );

//                                        .collect(Collectors.toList(),(x -> TimeUtil.isBetween(x.getDateTime().toLocalTime(),startTime, endTime))
//                                        .mapToInt(UserMeal::getCalories).sum() > caloriesPerDay))))
//                .filter(x -> TimeUtil.isBetween(x.getValue().forEach(e -> e.getDateTime().toLocalDate());getDateTime().toLocalTime(),startTime, endTime)
//        return null;
    }
}

