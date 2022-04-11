package com.iprism.elliot.data

import com.iprism.elliot.domain.model.Statistic
import com.iprism.elliot.domain.model.SubStatistic
import javax.inject.Inject

class Datasource @Inject constructor() {

    fun loadStatistics1(): List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Protein", "30"),
                    SubStatistic("Carbs", "300"),
                    SubStatistic("Fat", "124")
                )
            )
        )
    }

    fun loadStatistics2(): List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Protein", "120"),
                    SubStatistic("Carbs", "543"),
                    SubStatistic("Fat", "125")
                )
            )
        )
    }

    fun loadStatistics3(): List<Statistic> {
        return listOf(
            Statistic(
                listOf(
                    SubStatistic("Protein", "653"),
                    SubStatistic("Carbs", "23"),
                    SubStatistic("Fat", "24")
                )
            )
        )
    }
}