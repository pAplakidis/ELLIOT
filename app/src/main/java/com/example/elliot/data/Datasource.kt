package com.example.elliot.data

import com.example.elliot.domain.model.FoodModel
import com.example.elliot.domain.model.Meal
import com.example.elliot.domain.model.Statistic
import com.example.elliot.domain.model.SubStatistic

class Datasource {
    fun loadMeals(): List<Meal> {
        return listOf(
            Meal(
                "Breakfast", listOf(
                    FoodModel(0, "Apple"),
                    FoodModel(1, "Milk")
                )
            ),
            Meal(
                "Lunch", listOf(
                    FoodModel(3, "Chicken")
                )
            ),
            Meal(
                "Dinner", listOf(
                    FoodModel(4, "Toast")
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