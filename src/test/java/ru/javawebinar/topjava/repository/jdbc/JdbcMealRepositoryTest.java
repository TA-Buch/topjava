package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    private MealRepository repository;

    @Test
    public void testGet() {
        Meal result = repository.get(MEAL1_ID, USER_ID);
        assertNotNull(result);
        assertMatch(result, MEAL1);
    }

    @Test
    public void testGetNotFound() {
        assertNull(repository.get(999999, USER_ID));
    }

    @Test
    public void testDelete() {
        repository.delete(MEAL1_ID, USER_ID);
        assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testDeleteNotFound() {
        assertFalse(repository.delete(999999, USER_ID));
    }

    @Test
    public void testGetAll() {
        List<Meal> result = repository.getAll(USER_ID);
        assertMatch(result, MEAL1);
    }

    @Test
    public void testGetBetweenInclusive() {
        LocalDateTime startDate = LocalDate.of(2024, 2, 21).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2024, 2, 21).atTime(23, 59);

        List<Meal> result = repository.getBetweenHalfOpen(startDate, endDate, USER_ID);
        assertMatch(result, MEAL1);
    }

    @Test
    public void testCreate() {
        Meal newMeal = getNew();
        Meal created = repository.save(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);

        assertMatch(created, newMeal);
        assertMatch(repository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void testUpdate() {
        Meal updatedMeal = new Meal(MEAL1);
        updatedMeal.setDescription("Updated Lunch - Grilled Chicken with Salad");

        repository.save(updatedMeal, USER_ID);
        assertMatch(repository.get(MEAL1_ID, USER_ID), updatedMeal);
    }
}