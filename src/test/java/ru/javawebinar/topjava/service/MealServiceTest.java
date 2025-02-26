package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
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
public class MealServiceTest {


    @Autowired
    private MealService service;

    @Test
    public void testGet() {
        Meal result = service.get(MEAL1_ID, USER_ID);
        assertNotNull(result);
        assertMatch(MEAL1, result);
    }

    @Test    public void testGetNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(0, USER_ID));
    }

    @Test
    public void testDelete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(999999, USER_ID));
    }

    @Test
    public void testGetAll() {
        List<Meal> result = service.getAll(USER_ID);
        assertMatch(result, MEAL1);
    }

    @Test
    public void testGetAllEmpty() {
        List<Meal> result = service.getAll(11);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2024, 2, 21);
        LocalDate endDate = LocalDate.of(2024, 2, 21);

        List<Meal> result = service.getBetweenInclusive(startDate, endDate, USER_ID);
        assertMatch(result, MEAL1);
    }

    @Test
    public void testCreate() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void testUpdate() {
        Meal updatedMeal = MEAL1;
        updatedMeal.setDescription("Updated Lunch - Grilled Chicken with Salad");
        service.update(updatedMeal, USER_ID);
        Meal result = service.get(MEAL1_ID, USER_ID);
        assertMatch(updatedMeal, result);
    }
}