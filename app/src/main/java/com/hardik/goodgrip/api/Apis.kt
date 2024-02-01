package com.hardik.goodgrip.api

import com.hardik.goodgrip.models.AlbumResponse
import com.hardik.goodgrip.models.CommentResponse
import com.hardik.goodgrip.models.PhotoResponse
import com.hardik.goodgrip.models.PostResponse
import com.hardik.goodgrip.models.TodoResponse
import com.hardik.goodgrip.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface Apis {

    @GET("/posts")
    suspend fun getPosts() : Response<PostResponse>

    @GET("/comments")
    suspend fun getComments() : Response<CommentResponse>

    @GET("/albums")
    suspend fun getAlbums() : Response<AlbumResponse>

    @GET("/photos")
    suspend fun getPhotos() : Response<PhotoResponse>

    @GET("/todos")
    suspend fun getTodos() : Response<TodoResponse>

    @GET("/users")
    suspend fun getUsers() : Response<UserResponse>
}