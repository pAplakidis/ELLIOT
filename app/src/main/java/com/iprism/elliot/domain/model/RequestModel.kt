package com.iprism.elliot.domain.model

data class RequestModel(
    val token: String,
    val username: String,
    val date: String,
    val time: String
)
