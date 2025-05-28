package com.rabadiya.base.model.new_application

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
data class GetUserApplicationRequest(
    val deviceId: String
)

@Keep
data class GetNewApplicationResponse(
    var applicationsList: ArrayList<Applications> = arrayListOf()
)

@Keep
@Parcelize
data class Applications(
    var _id: String? = null,
    var deviceId: String? = null,
    var firstName: String? = null,
    var fatherHusbundName: String? = null,
    var birthDate: String? = null,
    var address: String? = null,
    var gender: String? = null,
    var mobileNo: String? = null,
    var emailId: String? = null,
    var password: String? = null,
    val occupation: String? = null,
    var businessType: String? = null,
    var eduction: String? = null,
    var relation: String? = null,
    val bloodGroup: String? = null,
    var maritalStatus: String? = null,
    var village: String? = null,
    var currentCity: String? = null,
    var document: String? = null,
    var profileImage: String? = null,
    var isReviewed: Boolean? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
) : Parcelable