package com.rabadiya.parivar.network_module.retrofit

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.parivar.network_module.model.CreateTokenResponse
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.newaccount.CreateNewApplicationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface RabadiyaApi {

    @FormUrlEncoded
    @POST("create-token")
    fun createToken(@Field("deviceId") deviceId: String): Call<CreateTokenResponse>

    @Multipart
    @POST("applications/create")
    suspend fun newApplication(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileImage: MultipartBody.Part?,
        @Part idProofFile: MultipartBody.Part?
    ): Response<BaseResponse<CreateNewApplicationResponse>>

    @POST("applications/getUserApplications")
    suspend fun getSubmittedApplications(
        @Body getUserApplicationRequest : GetUserApplicationRequest
    ) : Response<BaseResponse<GetNewApplicationResponse>>



}