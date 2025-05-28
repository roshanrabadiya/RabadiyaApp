package com.rabadiya.parivar.network_module.repository.admin

import com.rabadiya.parivar.network_module.domain.AdminApiHelper
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest

class AdminRepository(
    private val apiHelper: AdminApiHelper
) {
    suspend fun adminLogin(adminLoginRequest: AdminLoginRequest) = apiHelper.adminLogin(adminLoginRequest)

    suspend fun getAllApplications(
        isReview: Boolean,
        page: Int,
        limit: Int
    ) = apiHelper.getAllApplication(isReview, page, limit)

    suspend fun manageApplication(manageApplicationRequest: ManageApplicationRequest) =
        apiHelper.manageApplication(manageApplicationRequest)
}