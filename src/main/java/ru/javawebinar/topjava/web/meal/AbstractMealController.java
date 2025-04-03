package ru.javawebinar.topjava.web.meal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

public abstract class AbstractMealController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    protected int getAuthUserId() {
        return SecurityUtil.authUserId();
    }

    public Meal get(int id) {
        int userId = getAuthUserId();
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public String delete(int id) {
        int userId = getAuthUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "deleted";
    }

    public List<MealTo> getAll() {
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public String create(Meal meal) {
        int userId = getAuthUserId();
        checkIsNew(meal);
        log.info("create {} for user {}", meal, userId);
        service.create(meal, userId);
        return "created";
    }

    public void update(Meal meal, int id) {
        int userId = getAuthUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = getAuthUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}
