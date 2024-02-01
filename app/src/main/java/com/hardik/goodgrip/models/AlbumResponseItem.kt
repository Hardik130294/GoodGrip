package com.hardik.goodgrip.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "album")
data class AlbumResponseItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id: Int = 0, // 1
    @ColumnInfo(name = "user_id")
    @SerializedName("userId")
    @Expose
    val userId: Int = 0, // 1
    @SerializedName("title")
    @Expose
    val title: String = "" // quidem molestiae enim
)