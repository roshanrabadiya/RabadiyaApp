package com.rabadiya.parivar.network_module.domain

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginResponse
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import retrofit2.Response

interface AdminApiHelper {
    suspend fun adminLogin(adminLoginRequest: AdminLoginRequest):
            Response<BaseResponse<AdminLoginResponse>>

    suspend fun getAllApplication(
        isReview: Boolean,
        page: Int,
        limit: Int
    ): Response<BaseResponse<GetNewApplicationResponse>>

    suspend fun manageApplication(
        manageApplicationRequest: ManageApplicationRequest
    ): Response<BaseResponse<ManageApplicationResponse>>
}