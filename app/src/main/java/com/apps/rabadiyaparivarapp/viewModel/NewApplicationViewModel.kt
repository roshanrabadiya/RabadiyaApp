package com.apps.rabadiyaparivarapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.utils.TAG
import com.rabadiya.parivar.network_module.common.ApiConstants.RESULT_CREATED
import com.rabadiya.parivar.network_module.common.ApiConstants.RESULT_OK
import com.rabadiya.base.common.Resource
import com.rabadiya.base.model.new_application.GetNewApplicationResponse
import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.base.viewmodel.BaseViewModel
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.newaccount.CreateNewApplicationResponse
import com.rabadiya.parivar.network_module.repository.newaccount.NewApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File


class NewApplicationViewModel(
    private val repository: NewApplicationRepository,
) : BaseViewModel() {

    private val _applicationState =
        MutableStateFlow<Resource< BaseResponse<CreateNewApplicationResponse>>>(Resource.Success(null))
    val applicationState = _applicationState.asStateFlow()

    private val _checkApplication =
        MutableStateFlow<Resource<BaseResponse<GetNewApplicationResponse>>>(Resource.Success(null))
    val checkApplication = _checkApplication.asStateFlow()

    fun newApplication(params: HashMap<String, String>, idProofFile: File?, profileImage: File?) {
        _applicationState.value = Resource.Loading()
        viewModelScope.launch {
            runCatching {
                repository.newApplication(params = params, idProofFile = idProofFile, profileImage = profileImage)
            }.fold(onSuccess = {
                LOGI(TAG, "Api Call: ${it.code()}")
                if (it.isSuccessful && it.body() != null) {
                    LOGI(TAG, "Api Call: success ${it.body()}")
                    _applicationState.value = Resource.Success(it.body())
                } else {
                    LOGI(TAG, "Api Call: error ${it.body()}")
                    _applicationState.value = Resource.Error(it.message())
                }
            }, onFailure = {
                _applicationState.value = Resource.Error(it.message ?: "unknown error")
                Log.e(TAG, "error: $it")
            })
        }
    }

    fun checkApplications(deviceId: String) =
        makeApiCall(
            request = { repository.getSubmittedApplications(GetUserApplicationRequest(deviceId)) },
            stateFlow = _checkApplication,
            scope = viewModelScope
        )
}