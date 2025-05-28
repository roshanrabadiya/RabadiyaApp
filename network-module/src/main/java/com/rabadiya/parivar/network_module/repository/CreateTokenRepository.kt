package com.rabadiya.parivar.network_module.repository

import com.rabadiya.parivar.network_module.model.CreateTokenResponse
import com.rabadiya.parivar.network_module.retrofit.RabadiyaApi
import retrofit2.Call

class CreateTokenRepository(
    private val api: RabadiyaApi
) {
    fun createToken(deviceId: String): Call<CreateTokenResponse> = api.createToken(deviceId)
}