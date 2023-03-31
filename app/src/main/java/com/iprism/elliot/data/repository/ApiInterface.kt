package com.iprism.elliot.data.repository

import com.iprism.elliot.domain.model.RequestModel
import com.iprism.elliot.domain.model.ResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("elliotinfo")
    fun postRequest(@Body requestModel: RequestModel): Call<ResponseModel>
}