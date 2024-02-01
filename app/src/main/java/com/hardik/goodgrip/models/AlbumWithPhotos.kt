package com.hardik.goodgrip.models

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithPhotos(
    // One to many (Album to Photos) pairs
    @Embedded
    val album: AlbumResponseItem,
    @Relation(
        parentColumn = "id", // id of AlbumResponseItem (parent column)
        entityColumn = "album_id" // album_id of PhotoResponseItem (child column)
         )
    val photos: List<PhotoResponseItem>// returns a list of
)