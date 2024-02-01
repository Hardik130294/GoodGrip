package com.hardik.goodgrip.models

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithComments(
    // One to many (Post to Comments) pairs
    @Embedded
    val post: PostResponseItem,
    @Relation(
        parentColumn = "id", // id of PostResponseItem (parent column)
        entityColumn = "post_id" // post_id of CommentResponseItem (child column)
         )
    val comments: List<CommentResponseItem>// returns a list of
)
