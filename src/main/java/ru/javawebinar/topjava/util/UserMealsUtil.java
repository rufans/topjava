package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 900),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,9,30), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,12,30), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        List<UserMealWithExceed> filteredMealsWithExceeded = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredMealsWithExceeded.stream().forEach(System.out::println);
    }

    // 1 - method with two for loops
    /*public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // 1. passed through the mealList and added amount of calories for every day to the map
        Map<LocalDate, Integer> caloriesPerDateMap = new HashMap<>();

        LocalDate newDate = mealList.get(1).getDateTime().toLocalDate();
        int dayCalories = 0;

        for (UserMeal currentMeal : mealList) {
            LocalDate currentDate = currentMeal.getDateTime().toLocalDate();
            if (newDate.equals(currentDate)) {
                dayCalories += currentMeal.getCalories();
            } else {
                caloriesPerDateMap.put(newDate, dayCalories);

                newDate = currentDate;
                dayCalories = currentMeal.getCalories();
            }
        }

        caloriesPerDateMap.put(newDate, dayCalories);    // added the last day


        // 2. passed through the mealList and if current time of row isBetween, then added to resultList this meal
        List<UserMealWithExceed> resultList = new ArrayList<>();

        for (UserMeal currentMeal : mealList) {
            LocalTime currentTime = currentMeal.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(currentTime, startTime, endTime)) {
                LocalDateTime localDateTime = currentMeal.getDateTime();
                String description = currentMeal.getDescription();
                int calories = currentMeal.getCalories();
                boolean isGoalDone = false;

                LocalDate localDate = currentMeal.getDateTime().toLocalDate();
                int caloriesOnLocalDate = caloriesPerDateMap.get(localDate);
                if (caloriesOnLocalDate >= caloriesPerDay) {
                    isGoalDone = true;
                }

                resultList.add(new UserMealWithExceed(localDateTime, description, calories, isGoalDone));
            }
        }

        return resultList;
    }*/

    // method 2 - with Java 8 Stream API
    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // 1. passed through the mealList and added amount of calories for every day to the map
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        // 2. passed through the mealList and if current time of row isBetween, then added to resultList this meal
        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumByDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }
}
