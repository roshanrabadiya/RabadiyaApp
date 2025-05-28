package com.rabadiya.parivar.network_module.model.admin

data class ManageApplicationRequest(
    val applicationId: String,
    val firstName: String? = null,
    val fatherOrHusband: String? = null,
    val isApprove: Boolean,
    val disApproveReason: String
)