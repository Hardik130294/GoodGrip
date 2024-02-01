package com.hardik.goodgrip.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(
    tableName = "comment", foreignKeys = [ForeignKey(
        entity = PostResponseItem::class,
        parentColumns = ["id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CommentResponseItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id: Int = 0, // 1
    @ColumnInfo(name = "post_id")
    @SerializedName("postId")
    @Expose
    val postId: Int = 0, // 1
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("email")
    @Expose
    val email: String = "",
    @SerializedName("body")
    @Expose
    val body: String = ""
)