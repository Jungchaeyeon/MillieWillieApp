package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class GetUsersResponse(
    @SerializedName("result") val result: UserRes
): BaseResponse() {
    data class UserRes(
        val userId: Long?,
        val name: String?,
        val profileImg: String?,
        val birthday: String?
    )
}