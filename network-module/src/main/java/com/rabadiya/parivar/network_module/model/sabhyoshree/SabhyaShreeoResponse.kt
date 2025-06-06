package com.rabadiya.parivar.network_module.model.sabhyoshree

import com.rabadiya.parivar.network_module.model.base.Pagination

data class SabhyaShreeoResponse(
    val sabhyaShree: ArrayList<SabhyaShree>,
    val pagination: Pagination
)

data class SabhyaShree(
    val _id: String,
    val sabhyaNumber: Int,
    val username: String,
    val fatherOrHusband: String,
    val mobileNumber: String,
    val emailId: String,
    val deviceId: String,
    val dateOfBirth: String,
    val profileImage: String?,
    val village: String,
    val applicationId: String,
)