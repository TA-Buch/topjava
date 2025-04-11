package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;

@RestController
@RequestMapping(value = MealUIController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {
    static final String REST_URL = "/profile/meals";

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestBody Meal meal) {
        if (meal.getDateTime() == null) {
            meal.setDateTime(LocalDateTime.now());
        }

        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.getId());
        }
    }
    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate,
            @RequestParam @DateTimeFormat(iso = TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = TIME) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}