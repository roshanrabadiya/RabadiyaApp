package com.rabadiya.parivar.network_module.retrofit

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginResponse
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RabadiyaAdminApi {
    @POST("admin/adminLogin")
    suspend fun adminLogin(
        @Body adminLoginRequest: AdminLoginRequest
    ): Response<BaseResponse<AdminLoginResponse>>

    @GET("applications/admin/getAll")
    suspend fun getAllApplication(
        @Query("isReview") isReview: Boolean,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Response<BaseResponse<GetNewApplicationResponse>>

    @POST("admin/manageApplication")
    suspend fun manageApplication(
        @Body manageApplicationRequest: ManageApplicationRequest
    ) : Response<BaseResponse<ManageApplicationResponse>>
}