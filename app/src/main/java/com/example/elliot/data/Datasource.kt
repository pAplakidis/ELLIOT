package com.example.elliot.data

import com.example.elliot.domain.model.*
import javax.inject.Inject

class Datasource @Inject constructor(){

    fun loadMealsDemo(): List<HistoryModel> {
        return listOf(
            HistoryModel("Cereal", "08:56", "13/06", "breakfast"),
            HistoryModel("Kwriatiki", "13:56", "13/06", "lunch"),
            HistoryModel("Chickenaki", "20:56", "13/06", "dinner"),
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