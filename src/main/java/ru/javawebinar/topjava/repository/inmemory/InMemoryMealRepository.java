package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Meal>> userMeals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("User {}: save {}", userId, meal);
        userMeals.putIfAbsent(userId, new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet()); // Генерируем новый ID
        }

        userMeals.get(userId).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("User {}: delete meal {}", userId, mealId);
        return Optional.ofNullable(userMeals.get(userId))
                .map(meals -> meals.remove(mealId) != null)
                .orElse(false);
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("User {}: get meal {}", userId, mealId);
        return Optional.ofNullable(userMeals.get(userId))
                .map(meals -> meals.get(mealId))
                .orElse(null);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("User {}: get all meals", userId);
        return Optional.ofNullable(userMeals.get(userId))
                .map(meals -> meals.values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}

