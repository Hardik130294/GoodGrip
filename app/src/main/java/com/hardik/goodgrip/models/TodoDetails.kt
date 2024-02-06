package com.hardik.goodgrip.models

data class TodoDetails(
    val todoId: Int,
    val todoTitle: String,
    val completed: Boolean,
    val userId: Int,
    val userName:String
)
