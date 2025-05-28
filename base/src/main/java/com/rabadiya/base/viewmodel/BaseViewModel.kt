package com.rabadiya.base.viewmodel

import androidx.lifecycle.ViewModel
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.common.Resource
import com.rabadiya.base.utils.Constants.RESULT_CODE_OK
import com.rabadiya.base.utils.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

open class BaseViewModel : ViewModel() {

    /**
     * A generic function to handle API calls with common error handling and response processing
     *
     * @param T The type of the response body
     * @param request The API call suspending function that returns a Response<T>
     * @param stateFlow The MutableStateFlow to update with the Resource state
     * @param scope The CoroutineScope to launch the API call in
     * @param successCode The HTTP status code that indicates success (default is 200)
     * @param errorHandler Optional custom error handler for the response
     */
    fun <T> makeApiCall(
        request: suspend () -> Response<T>,
        stateFlow: MutableStateFlow<Resource<T>>,
        scope: CoroutineScope,
        successCode: Int = RESULT_CODE_OK,
        errorHandler: ((Response<T>) -> String)? = null
    ) {
        scope.launch {
            stateFlow.value = Resource.Loading()
            try {
                val response = request()
                if (response.isSuccessful && response.body() != null && response.code() == successCode) {
                    stateFlow.value = Resource.Success(response.body())
                } else {
                    val errorMessage = errorHandler?.invoke(response) ?: extractErrorMessage(response)
                    stateFlow.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                LOGI(TAG, "makeApiCall: ${e.message}")
                stateFlow.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    /**
     * Extract error message from the error body
     */
    private fun <T> extractErrorMessage(response: Response<T>): String {
        val errorBody = response.errorBody()?.string()
        return try {
            errorBody?.let { JSONObject(it).getString("errorMessage") } ?: "Unknown error occurred"
        } catch (e: Exception) {
            errorBody ?: "Unknown error occurred (${response.code()})"
        }
    }

}