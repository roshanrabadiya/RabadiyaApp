package com.rrr.app_admin_module.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Resource
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.admin.AdminLoginRequest
import com.rabadiya.parivar.network_module.model.admin.AdminLoginResponse
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.repository.admin.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AdminLoginViewModel(
    private val adminRepository: AdminRepository
) : BaseViewModel() {

    private val _adminLoginResponse = MutableStateFlow<Resource<BaseResponse<AdminLoginResponse>>>(Resource.Success(null))
    val adminLoginResponse = _adminLoginResponse.asStateFlow()

    fun adminLogin(username: String, password: String) {
        makeApiCall(
            request = { adminRepository.adminLogin(AdminLoginRequest(username, password)) },
            stateFlow = _adminLoginResponse,
            scope = viewModelScope
        )
    }
}