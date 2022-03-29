package com.example.elliot.data

import com.example.elliot.domain.model.*
import javax.inject.Inject

class Datasource @Inject constructor() {

    fun loadMealsDemo(): List<CardModel> {
        return listOf(
            CardModel(
                "Breakfast",
                mutableListOf(
                    FoodModel(
                        "Kwriatiki",
                        "16/03/2022",
                        listOf("Tomatoula", "Aggouraki")
                    ),
                    FoodModel(
                        "Test",
                        "16/03/2022",
                        listOf("Tomatoula", "Aggouraki")
                    )
                )
            ),
            CardModel(
                "Lunch",
                mutableListOf(
                    FoodModel(
                        "Kot Patataoua",
                        "16/03/2022",
                        listOf("kota", "patates")
                    )
                )
            ),
            CardModel(
                "Dinner",
                mutableListOf(
                    FoodModel(
                        "Makaronia me kimako",
                        "16/03/2022",
                        listOf("makaronakia", "kimakos")
                    )
                )
            )
        )
    }

    fun loadStatistics1(): List<Statistic> {
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

    fun loadStatistics2(): List<Statistic> {
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

    fun loadStatistics3(): List<Statistic> {
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