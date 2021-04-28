package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ScheduleRequest(
    @SerializedName("color") var color: String,
    @SerializedName("distinction") var distinction: String,
    @SerializedName("title") var title: String,
    @SerializedName("startDate") var startDate: String,
    @SerializedName("endDate") var endDate: String,
    @SerializedName("repetition") var repetition: String,
    @SerializedName("push") var push: String,
    @SerializedName("pushDeviceToken") var pushDeviceToken: String
) : Serializable