package com.example.elliot.data

import com.example.elliot.model.FoodDetail
import com.example.elliot.model.Meal
import com.example.elliot.model.Statistic
import com.example.elliot.model.SubStatistic

class Datasource {
    fun loadMeals(): List<Meal> {
        return listOf(
            Meal(
                "Breakfast", listOf(
                    FoodDetail("Apple", "12:00"),
                    FoodDetail("Milk", "12:00")
                )
            ),
            Meal(
                "Lunch", listOf(
                    FoodDetail("Chicken", "15:00")
                )
            ),
            Meal(
                "Dinner", listOf(
                    FoodDetail("Toast", "21:00")
                )
            )
        )
    }

    fun loadStatistics1() : List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Mobile Phones", "30"),
                    SubStatistic("Mobile Apps", "300"),
                    SubStatistic("Consoles", "124")
                )
            )
        )
    }

    fun loadStatistics2() : List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Kinder", "30"),
                    SubStatistic("Lacta", "23"),
                    SubStatistic("Ion", "56"),
                    SubStatistic("Milka", "72")
                )
            )
        )
    }

    fun loadStatistics3() : List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Football", "235"),
                    SubStatistic("Basketball", "678"),
                )
            )
        )
    }
}