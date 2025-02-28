package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            int updatedRows = em.createNamedQuery(Meal.UPDATE)
                    .setParameter("id", meal.getId())
                    .setParameter("user", ref)
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("description", meal.getDescription())
                    .executeUpdate();

            if (updatedRows == 0) {
                return null;
            }
            return meal;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        User ref = em.getReference(User.class, userId);
        int deletedRows = em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user", ref)
                .executeUpdate();

        return deletedRows != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        User ref = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.GET_BY_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("user", ref)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        User ref = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.GET_BY_USER, Meal.class)
                .setParameter("user", ref)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        User ref = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("user", ref)
                .setParameter("startDate", startDateTime)
                .setParameter("endDate", endDateTime)
                .getResultList();
    }
}