package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                             Model model)  {
        List<MealTo> meals = super.getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable int id, Model model) {
        Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String save(@RequestParam(required = false) Integer id, @RequestParam String description,
                       @RequestParam int calories, @RequestParam String dateTime) {
        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, id);
        }
        return "redirect:/meals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        super.delete(id);
        return "redirect:/meals";
    }

}
