package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PatchUsersResponse(
    @SerializedName("name") val result: UserRes
): BaseResponse() {
    data class UserRes(
        val userId: Long,
        val name: String,
        val profileImg: String,
        val birthday: String
    )
}