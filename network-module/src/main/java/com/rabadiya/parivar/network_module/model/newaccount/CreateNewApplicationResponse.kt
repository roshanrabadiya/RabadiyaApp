package com.rabadiya.parivar.network_module.model.newaccount

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateNewApplicationResponse(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("applicationId")
    var applicationId: Int?,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("error")
    var error: String = ""
)