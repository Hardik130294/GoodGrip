package com.hardik.goodgrip.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Keep
@Entity(tableName = "post", foreignKeys = [ForeignKey(
    entity = UserResponseItem::class,
    parentColumns = ["id"],
    childColumns = ["user_id"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)])
data class PostResponseItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id: Int= 0, // 1
    @ColumnInfo(name = "user_id")
    @SerializedName("userId")
    @Expose
    val userId: Int= 0, // 1
    @SerializedName("body")
    @Expose
    val body: String= "",
    @SerializedName("title")
    @Expose
    val title: String= ""
)