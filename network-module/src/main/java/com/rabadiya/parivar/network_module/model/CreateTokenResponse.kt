package com.rabadiya.parivar.network_module.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateTokenResponse(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("error")
    var error: String = ""
)
