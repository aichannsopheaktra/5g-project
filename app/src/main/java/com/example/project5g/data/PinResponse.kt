package com.example.project5g.data

import com.google.gson.annotations.SerializedName

data class PinResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("code")
    val code: String?,
    @SerializedName("customer_id")
    val customerId: String?,
    @SerializedName("detail")
    val detail: String?
)

