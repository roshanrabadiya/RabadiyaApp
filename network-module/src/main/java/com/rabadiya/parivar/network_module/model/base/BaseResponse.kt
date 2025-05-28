package com.rabadiya.parivar.network_module.model.base

data class BaseResponse<T>(
    val status: Boolean,
    val statusCode: Int,
    val message: String? = null,
    val data: T? = null,
    val errorMessage: String? = null
)
