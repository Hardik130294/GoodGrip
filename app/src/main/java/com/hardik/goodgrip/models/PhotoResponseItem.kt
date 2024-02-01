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
    tableName = "photo", foreignKeys = [ForeignKey(
        entity = AlbumResponseItem::class,
        parentColumns = ["id"],
        childColumns = ["album_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PhotoResponseItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id: Int = 0, // 1
    @ColumnInfo(name = "album_id")
    @SerializedName("albumId")
    @Expose
    val albumId: Int = 0, // 1
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("thumbnailUrl")
    @Expose
    val thumbnailUrl: String = ""
)