package com.rabadiya.parivar.network_module.common

import android.content.Context
import android.util.Log
import com.rabadiya.base.common.AppPreference
import com.rabadiya.base.common.PreferenceKey
import com.rabadiya.base.utils.API_STATUS_CODE_SUCCESS
import com.rabadiya.base.utils.API_STATUS_SUCCESS
import com.rabadiya.base.utils.backgroundScope
import com.rabadiya.base.utils.fetchDeviceId
import com.rabadiya.base.utils.showToast
import com.rabadiya.parivar.network_module.common.ApiConstants.AUTH_TOKEN_TYPE
import com.rabadiya.parivar.network_module.model.CreateTokenResponse
import com.rabadiya.parivar.network_module.repository.CreateTokenRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenManager(
    private val repository: CreateTokenRepository,
    private val appPreference: AppPreference,
    private val  context: Context
) {

    /**
     * fetch token from the api and save it in preference
     * */
    fun fetchToken(resultCallBack: (String) -> Unit) {
        val prefApiToken = appPreference.getStringData(PreferenceKey.KEY_API_TOKEN)
        if (prefApiToken.isEmpty()) {
            backgroundScope.launch {
                repository.createToken(context.fetchDeviceId())
                    .enqueue(object : Callback<CreateTokenResponse> {
                        override fun onResponse(
                            call: Call<CreateTokenResponse>,
                            response: Response<CreateTokenResponse>
                        ) {
                            val responseBody = response.body()
                            if (response.isSuccessful && response.code() == API_STATUS_CODE_SUCCESS) {
                                if (responseBody?.status == API_STATUS_SUCCESS) {
                                    appPreference.saveData(
                                        PreferenceKey.KEY_API_TOKEN,
                                        responseBody.token
                                    )
                                    resultCallBack(responseBody.token)
                                }
                            } else {
                                context.showToast(responseBody?.error ?: "Something went wrong")
                                resultCallBack("")
                            }
                        }

                        override fun onFailure(call: Call<CreateTokenResponse>, t: Throwable) {
                            Log.e("ERROR", "(fetchToken) onFailure: ${t.message}")
                            resultCallBack("")
                        }

                    })
            }
        }
    }

    /**
     * get token from preference
     * */
    private fun getToken(): String {
        return appPreference.getStringData(PreferenceKey.KEY_API_TOKEN)
    }

    /**
     * check token is exist in preference
     * */
    fun checkToken(): Boolean {
        return getToken().isNotEmpty()
    }

    /**
     * get auth token
     * */
    fun getAuthToken(): String {
        return if (checkToken()) {
            "$AUTH_TOKEN_TYPE ${getToken()}"
        } else {
            var token = ""
            fetchToken {
                token = "$AUTH_TOKEN_TYPE $it"
            }
            token
        }
    }
}