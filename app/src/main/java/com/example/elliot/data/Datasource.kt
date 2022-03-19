package com.example.elliot.data

import com.example.elliot.domain.model.*
import javax.inject.Inject

class Datasource @Inject constructor(){
//    fun loadMeals(): List<Meal> {
//        return listOf(
//            Meal(
//                "Breakfast", listOf(
//                    FoodModel(0, "Apple"),
//                    FoodModel(1, "Milk")
//                )
//            ),
//            Meal(
//                "Lunch", listOf(
//                    FoodModel(3, "Chicken")
//                )
//            ),
//            Meal(
//                "Dinner", listOf(
//                    FoodModel(4, "Toast")
//                )
//            )
//        )
//    }

    fun loadMealsDemo(): List<CardModel> {
        return listOf(
            CardModel("Breakfast",
                mutableListOf(
                    FoodModel(1, "Kwriatiki",
                    "16/03/2022",
                    mutableListOf("Tomatoula", "Aggouraki")),
                    FoodModel(4, "Test",
                        "16/03/2022",
                        mutableListOf("Tomatoula", "Aggouraki"))
                )
            ),
            CardModel("Lunch",
                mutableListOf(
                    FoodModel(2, "Kot Patataoua",
                        "16/03/2022",
                        mutableListOf("kota", "patates"))
                )
            ),
            CardModel("Dinner",
                mutableListOf(
                    FoodModel(3, "Makaronia me kimako",
                        "16/03/2022",
                        mutableListOf("makaronakia", "kimakos"))
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