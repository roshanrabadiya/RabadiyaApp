package com.rabadiya.parivar.network_module.model.admin

import androidx.annotation.Keep

@Keep
data class AdminLoginRequest (
    val username: String,
    val password: String
)