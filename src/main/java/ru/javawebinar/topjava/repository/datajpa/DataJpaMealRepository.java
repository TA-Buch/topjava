package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return crudUserRepository.findById(userId)
                .flatMap(user -> {
                    if (meal.getId() != null && crudRepository.findByIdAndUser(meal.getId(), user) == null) {
                        return Optional.empty();
                    }
                    meal.setUser(user);
                    return Optional.of(crudRepository.save(meal));
                })
                .orElse(null);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, crudUserRepository.getReferenceById(userId)) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findByIdAndUser(id, crudUserRepository.getReferenceById(userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUser(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
