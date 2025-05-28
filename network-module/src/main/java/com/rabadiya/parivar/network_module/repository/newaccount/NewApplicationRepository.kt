package com.rabadiya.parivar.network_module.repository.newaccount

import com.rabadiya.base.model.new_application.GetUserApplicationRequest
import com.rabadiya.parivar.network_module.model.base.BaseResponse
import com.rabadiya.parivar.network_module.model.newaccount.CreateNewApplicationResponse
import com.rabadiya.parivar.network_module.retrofit.RabadiyaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class NewApplicationRepository(
    private val api: RabadiyaApi,
) {
    suspend fun newApplication(
        params: HashMap<String, String>,
        idProofFile: File?,
        profileImage: File?
    ): Response<BaseResponse<CreateNewApplicationResponse>> {
        return withContext(Dispatchers.IO) {
            val paramMap = params.mapValues { (_, value) ->
                value.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val uploadIdProof = idProofFile?.let {
                MultipartBody.Part.createFormData(
                    "document",
                    it.name,
                    it.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            val uploadProfileImage = profileImage?.let {
                MultipartBody.Part.createFormData(
                    "profileImage",
                    it.name,
                    it.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            api.newApplication(paramMap, uploadProfileImage, uploadIdProof)
        }
    }

    suspend fun getSubmittedApplications(getUserApplicationRequest: GetUserApplicationRequest) =
        api.getSubmittedApplications(getUserApplicationRequest)
}