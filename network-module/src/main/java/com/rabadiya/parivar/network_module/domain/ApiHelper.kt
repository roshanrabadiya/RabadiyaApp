package com.rabadiya.parivar.network_module.domain

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.sabhyoshree.SabhyaShreeoResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getSubmittedApplications(getUserApplicationRequest: GetUserApplicationRequest):
            Response<BaseResponse<GetNewApplicationResponse>>

    suspend fun getSabhyaShreeo(
        search: String,
        page: Int,
        limit: Int
    ): Response<BaseResponse<SabhyaShreeoResponse>>
}