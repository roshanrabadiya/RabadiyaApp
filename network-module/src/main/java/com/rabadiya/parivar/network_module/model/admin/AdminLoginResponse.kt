package com.rabadiya.parivar.network_module.model.admin

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AdminLoginResponse(
    @SerializedName("id") val id : String,
    @SerializedName("username") val username : String,
)
