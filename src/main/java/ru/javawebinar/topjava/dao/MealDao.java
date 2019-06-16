package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.db.DBUtil;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

    public class MealDao {

        private Connection connection;

        public MealDao() {
            connection = DBUtil.getConnection();
        }

        public void addMeal(Meal meal) {
            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into meals(id_meal, datitime, description, calories) values (?, ?, ?, ? )");
                // Parameters start with 1
                preparedStatement.setRowId(1, null);
                preparedStatement.setDate(2, new java.sql.Date(meal.getDateTime().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()));
                preparedStatement.setString(3, meal.getDescription());
                preparedStatement.setInt(4, meal.getCalories());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteMeal(int mealId) {
            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("delete from meals where id_meal=?");
                // Parameters start with 1
                preparedStatement.setInt(1, mealId);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateMeal(Meal meal) {
            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("update meals set datitime=?, description=?, calories=? " +
                                "where id_meal=?");
                // Parameters start with 1
                preparedStatement.setDate(1, new java.sql.Date(meal.getDateTime().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()));
                preparedStatement.setString(2, meal.getDescription());
                preparedStatement.setInt(3, meal.getCalories());
                preparedStatement.setInt(4, meal.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public List<Meal> getAllMeals() {
            List<Meal> meals = new ArrayList<Meal>();
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM meals");
                while (rs.next()) {
                    Meal meal = new Meal(rs.getInt("id_meal"),
                            LocalDateTime.ofInstant(rs.getTimestamp("datetime").toInstant(),ZoneOffset.ofTotalSeconds(0)),
                            rs.getString("description"), rs.getInt("calories"));
                    meals.add(meal);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return meals;
        }

        public Meal getUserById(int mealId) {
            Meal meal = null;
            try {
                PreparedStatement preparedStatement = connection.
                        prepareStatement("select * from meals where id_meal=?");
                preparedStatement.setInt(1, mealId);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    meal = new Meal(rs.getInt("id_meal"),
                            LocalDateTime.ofInstant(rs.getTimestamp("datetime").toInstant(),ZoneOffset.ofTotalSeconds(0)),
                            rs.getString("description"),
                            rs.getInt("calories"));
                                   }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return meal;
        }
    }
