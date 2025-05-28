package com.rabadiya.parivar.network_module.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rabadiya.base.common.AppPreference
import com.rabadiya.parivar.network_module.common.TokenManager
import com.rabadiya.parivar.network_module.data.AdminApiHelperImpl
import com.rabadiya.parivar.network_module.data.ApiHelperImpl
import com.rabadiya.parivar.network_module.domain.AdminApiHelper
import com.rabadiya.parivar.network_module.domain.ApiHelper
import com.rabadiya.parivar.network_module.repository.CreateTokenRepository
import com.rabadiya.parivar.network_module.repository.admin.AdminRepository
import com.rabadiya.parivar.network_module.repository.newaccount.NewApplicationRepository
import com.rabadiya.parivar.network_module.retrofit.AuthInterceptor
import com.rabadiya.parivar.network_module.retrofit.RabadiyaAdminApi
import com.rabadiya.parivar.network_module.retrofit.RabadiyaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { AppPreference(get()) }
    single { AuthInterceptor(get()) }
    factory { provideOkHttpClient(get()) }
    factory { provideBaseURL() }
    factory { provideRetrofit(get(), get()) }
    factory { provideRabadiyaApi(get()) }
    factory { provideRabadiyaAdminApi(get()) }
    factory { TokenManager(get(), get(), get()) }

    single { CreateTokenRepository(get()) }
    single<ApiHelper> {
        ApiHelperImpl(get())
    }
    single<AdminApiHelper> { AdminApiHelperImpl(get()) }

    // Repository
    single { NewApplicationRepository(get()) }
    single { AdminRepository(get()) }
}

fun provideBaseURL(): String {
    return "http://192.168.1.5:5500/api/v1/" //BuildConfig.BASE_URL
}

fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor(authInterceptor)
        .build()
}

fun provideRabadiyaApi(retrofit: Retrofit): RabadiyaApi = retrofit.create(RabadiyaApi::class.java)

fun provideRabadiyaAdminApi(retrofit: Retrofit): RabadiyaAdminApi =
    retrofit.create(RabadiyaAdminApi::class.java)