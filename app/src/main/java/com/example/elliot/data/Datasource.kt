package com.example.elliot.data

import com.example.elliot.domain.model.*

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

//    TODO
//     "COMPLETE THE IMPLEMENTATION" +
//     "VIEWMODEL FUNCTION" +
//     "INJECT TO RECYCLED VIEW"

    fun loadMealsDemo(): List<CardModel> {
        return listOf(
            CardModel(
                1,
                mutableListOf("Tomatoula", "Aggouraki"),
                "16/03/2022",
                "Kwriatiki"
            ),
            CardModel(
                2,
                mutableListOf("Kotopoylo", "Patates"),
                "16/03/2022",
                "Kot patataoua"
            ),
            CardModel(
                3,
                mutableListOf("Makaronia", "Kimakos"),
                "16/03/2022",
                "Makaronia me kimako"
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