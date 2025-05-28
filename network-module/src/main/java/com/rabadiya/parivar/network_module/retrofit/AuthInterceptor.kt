package com.rabadiya.parivar.network_module.retrofit

import com.rabadiya.base.common.AppPreference
import com.rabadiya.base.common.PreferenceKey
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val appPreference: AppPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = appPreference.getStringData(PreferenceKey.KEY_API_TOKEN)

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
       return chain.proceed(newRequest)
    }
}