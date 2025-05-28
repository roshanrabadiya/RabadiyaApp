package com.rabadiya.parivar.network_module.domain

import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import retrofit2.Response

interface ApiHelper {


    suspend fun getSubmittedApplications(getUserApplicationRequest: GetUserApplicationRequest): Response<BaseResponse<GetNewApplicationResponse>>
}