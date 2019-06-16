package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/meals.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDao();
    }


    @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            log.debug("redirect to meals");

//            List<MealTo> mealToList = MealsUtil.getFilteredWithExcess(Meal.inicializeMeals(), LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//            request.setAttribute("mealToList", mealToList);
//        request.getRequestDispatcher("/meals.jsp").forward(request, response);

        String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int mealId = Integer.parseInt(request.getParameter("id_meal"));
            dao.deleteMeal(mealId);
            forward = LIST_MEAL;
            request.setAttribute("mealToList", MealsUtil.getFilteredWithExcess(dao.getAllMeals(),LocalTime.MIN, LocalTime.MAX, 2000));
        }
          else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id_meal"));
            Meal meal = dao.getUserById(mealId);
//            request.setAttribute("user", user);
        } else if (action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;
            request.setAttribute("mealToList", MealsUtil.getFilteredWithExcess(dao.getAllMeals(),LocalTime.MIN, LocalTime.MAX, 2000));
        } /**/else {
            forward = INSERT_OR_EDIT;
        }

        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealid = request.getParameter("id_meal");
        if(mealid == null || mealid.isEmpty())
        {
            dao.addMeal(new Meal(null, dateTime, description, calories));
        }
        else
        {
            int mealid = Integer.parseInt(request.getParameter("id_meal"));
            Meal meal = new Meal(mealid, dateTime, description, calories);
            dao.updateMeal(mealid);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL;
        request.setAttribute("users", dao.getAllMeals());
        view.forward(request, response);
    }
}
