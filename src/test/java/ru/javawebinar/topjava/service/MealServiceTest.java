package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(DINER_ID, USER_ID);
        assertMatch(meal, MEAL_DINER);
    }

    @Test
    public void delete() throws Exception {
        service.delete(DINER_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL_BREAKFAST);
    }

    @Test(expected = NotFoundException.class)
    public void ExceptionOnGetOtherUserMeal() throws Exception {
        Meal meal = service.get(DINER_ID, USER_ID - 1);
        assertMatch(meal, MEAL_DINER);
    }

    @Test(expected = NotFoundException.class)
    public void ExceptionOnDeleteOtherUserMeal() throws Exception {
        service.delete(DINER_ID, USER_ID - 1);
        assertMatch(service.getAll(USER_ID), MEAL_BREAKFAST);
    }

    @Test(expected = NotFoundException.class)
    public void ExceptionOnUpdateOtherUserMeal() throws Exception {
        Meal updatedMeal = new Meal(MEAL_BREAKFAST);
        updatedMeal.setCalories(800);
        updatedMeal.setDescription("ужин");
        service.update(updatedMeal, USER_ID-1);
        assertMatch(service.get(BREAKFAST_ID, USER_ID), updatedMeal);
    }

    @Test
    public void getBetweenDates() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY,  30),
                LocalDate.of(2015, Month.MAY,  31),
                USER_ID), MEAL_DINER, MEAL_BREAKFAST);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        assertMatch(service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY,  30, 9, 0),
                LocalDateTime.of(2015, Month.MAY,  30, 11, 0),
                USER_ID), MEAL_BREAKFAST);
    }

    @Test
    public void getAll()  throws Exception {
        List<Meal> mealList = service.getAll(USER_ID);
        assertMatch(mealList, MEAL_DINER, MEAL_BREAKFAST);
    }

    @Test
    public void update() throws Exception {
        Meal updatedMeal = new Meal(MEAL_BREAKFAST);
        updatedMeal.setCalories(800);
        updatedMeal.setDescription("ужин");
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(BREAKFAST_ID, USER_ID), updatedMeal);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, new Date(), "lunch", 1005);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, MEAL_DINER, MEAL_BREAKFAST);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() throws Exception {
        service.create(new Meal(null,
                LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "lunch", 1005), USER_ID);
    }

}