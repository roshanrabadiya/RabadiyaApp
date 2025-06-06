package com.rabadiya.parivar.network_module.data

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.parivar.network_module.domain.ApiHelper
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.sabhyoshree.SabhyaShreeoResponse
import com.rabadiya.parivar.network_module.retrofit.RabadiyaApi
import retrofit2.Response

class ApiHelperImpl(
    private val apiService: RabadiyaApi
) : ApiHelper {

    override suspend fun getSubmittedApplications(getUserApplicationRequest: GetUserApplicationRequest) =
        apiService.getSubmittedApplications(getUserApplicationRequest)

    override suspend fun getSabhyaShreeo(
        search: String,
        page: Int,
        limit: Int
    ): Response<BaseResponse<SabhyaShreeoResponse>> =
        apiService.getSabhyaShreeo(search, page, limit)

}