package com.hardik.goodgrip.models

data class CommentDetails(
    val commentId: Int,
    val commentName: String,
    val commentEmail: String,
    val postId: Int,
    val postTitle: String,
    val userId: Int,
    val userName: String,
    val userCity: String,
    val userEmail: String
)