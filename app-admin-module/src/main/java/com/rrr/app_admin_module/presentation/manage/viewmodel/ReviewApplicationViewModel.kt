package com.rrr.app_admin_module.presentation.manage.viewmodel

import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Resource
import com.rabadiya.base.utils.Constants.RESULT_CODE_CREATED
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationRequest
import com.rabadiya.parivar.network_module.model.admin.ManageApplicationResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.repository.admin.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReviewApplicationViewModel(
    private val repository: AdminRepository
) : BaseViewModel() {

    private val _manageApplicationResponse =
        MutableStateFlow<Resource<BaseResponse<ManageApplicationResponse>>>(Resource.Success(null))
    val manageApplicationResponse = _manageApplicationResponse.asStateFlow()

    fun manageApplication(manageApplicationRequest: ManageApplicationRequest) {
        makeApiCall(
            request = { repository.manageApplication(manageApplicationRequest) },
            stateFlow = _manageApplicationResponse,
            successCode = RESULT_CODE_CREATED,
            scope = viewModelScope
        )
    }
}