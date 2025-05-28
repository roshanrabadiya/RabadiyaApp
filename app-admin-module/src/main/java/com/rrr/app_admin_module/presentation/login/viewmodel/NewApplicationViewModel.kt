package com.rrr.app_admin_module.presentation.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Resource
import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.repository.admin.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewApplicationViewModel(
    private val repository: AdminRepository,
) : BaseViewModel() {

    private val _allApplications =
        MutableStateFlow<Resource<BaseResponse<GetNewApplicationResponse>>>(Resource.Success(null))
    val allApplication = _allApplications.asStateFlow()

    fun getApplications(isReview: Boolean, page: Int, limit: Int) {
        makeApiCall(
            request = { repository.getAllApplications(isReview, page, limit) },
            stateFlow = _allApplications,
            scope = viewModelScope
        )
    }
}