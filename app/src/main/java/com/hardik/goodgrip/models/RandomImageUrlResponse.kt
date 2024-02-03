package com.hardik.goodgrip.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class RandomImageUrlResponse(
    @SerializedName("url")
    @Expose
    val imageUrl: String = "" // https://ozgrozer.github.io/100k-faces/0/0/000697.jpg

)
