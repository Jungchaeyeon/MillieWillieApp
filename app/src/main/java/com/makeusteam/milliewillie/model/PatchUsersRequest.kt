package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PatchUsersRequest(
    @SerializedName("name") val name:String,
    @SerializedName("profileImg") val profileImg:String? = null,
    @SerializedName("birthday") val birthday:String? = null
)