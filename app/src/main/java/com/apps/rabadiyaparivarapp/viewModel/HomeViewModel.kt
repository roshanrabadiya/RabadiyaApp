package com.apps.rabadiyaparivarapp.viewModel

import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Resource
import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.repository.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

}