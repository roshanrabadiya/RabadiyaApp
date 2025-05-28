package com.rabadiya.parivar.network_module.data

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.parivar.network_module.domain.AdminApiHelper
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.retrofit.RabadiyaAdminApi
import retrofit2.Response

class AdminApiHelperImpl(
    private val apiService: RabadiyaAdminApi
) : AdminApiHelper {
    override suspend fun adminLogin(adminLoginRequest: AdminLoginRequest) =
        apiService.adminLogin(adminLoginRequest)

    override suspend fun getAllApplication(
        isReview: Boolean,
        page: Int,
        limit: Int
    ): Response<BaseResponse<GetNewApplicationResponse>> =
        apiService.getAllApplication(isReview, page, limit)

    override suspend fun manageApplication(manageApplicationRequest: ManageApplicationRequest):
            Response<BaseResponse<ManageApplicationResponse>> =
        apiService.manageApplication(manageApplicationRequest)
}