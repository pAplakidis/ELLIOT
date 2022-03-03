package com.example.elliot.data

import com.example.elliot.domain.model.Food
import com.example.elliot.domain.model.Meal
import com.example.elliot.domain.model.Statistic
import com.example.elliot.domain.model.SubStatistic

class Datasource {
    fun loadMeals(): List<Meal> {
        return listOf(
            Meal(
                "Breakfast", listOf(
                    Food("Apple"),
                    Food("Milk")
                )
            ),
            Meal(
                "Lunch", listOf(
                    Food("Chicken")
                )
            ),
            Meal(
                "Dinner", listOf(
                    Food("Toast")
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