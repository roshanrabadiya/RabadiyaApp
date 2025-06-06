package com.apps.rabadiyaparivarapp.presentation.detail.viewmodel

import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Resource
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.sabhyoshree.SabhyaShreeoResponse
import com.rabadiya.parivar.network_module.repository.sabhyoshree.SabhyaShreeoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SabhyaShreeoViewModel(
    private val repo: SabhyaShreeoRepository
) : BaseViewModel() {

    private val _sabhyaShreeoResponse =
        MutableStateFlow<Resource<BaseResponse<SabhyaShreeoResponse>>>(Resource.Success(null))
    val sabhyaShreeoResponse = _sabhyaShreeoResponse.asStateFlow()

    fun getSabhyaShreeo(search: String, page: Int, limit: Int) {
        makeApiCall(
            request = { repo.getSabhyaShreeo(search, page, limit) },
            stateFlow = _sabhyaShreeoResponse,
            scope = viewModelScope
        )
    }
}