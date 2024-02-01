package com.hardik.goodgrip.repository

import com.hardik.goodgrip.api.RetrofitInstance
import com.hardik.goodgrip.db.RoomDatabaseInstance
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.PostWithComments
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponseItem

class RepositoryInstance (val db : RoomDatabaseInstance){
    val TAG = RepositoryInstance::class.java.simpleName

    // Post
    suspend fun getPosts() = RetrofitInstance.api.getPosts()

    suspend fun upsertPost(post: PostResponseItem) = db.getRoomDatabaseDao().upsertPost(post)

    fun getAllPosts() = db.getRoomDatabaseDao().getAllPosts()

    suspend fun deletePosts(post: PostResponseItem) = db.getRoomDatabaseDao().deletePost(post)

    // Post with Comments
    fun getPostWithComments(postId: Int) = db.getRoomDatabaseDao().getPostWithComments(postId)

    // Comment
    suspend fun getComments() = RetrofitInstance.api.getComments()

    suspend fun upsertComment(comment: CommentResponseItem) = db.getRoomDatabaseDao().upsertComment(comment)

    fun getAllComments() = db.getRoomDatabaseDao().getAllComments()

    suspend fun deleteComments(comment: CommentResponseItem) = db.getRoomDatabaseDao().deleteComment(comment)

    // Album
    suspend fun getAlbums() = RetrofitInstance.api.getAlbums()
    suspend fun upsertAlbum(album: AlbumResponseItem) = db.getRoomDatabaseDao().upsertAlbum(album)
    fun getAllAlbums() = db.getRoomDatabaseDao().getAllAlbums()
    suspend fun deleteAlbum(album: AlbumResponseItem) = db.getRoomDatabaseDao().deleteAlbum(album)

    // Album with Photos
    fun getAlbumWithPhotos(albumId: Int) = db.getRoomDatabaseDao().getAlbumWithPhotos(albumId)

    // Photo
    suspend fun getPhotos() = RetrofitInstance.api.getPhotos()
    suspend fun upsertPhoto(photo: PhotoResponseItem) = db.getRoomDatabaseDao().upsertPhoto(photo)
    fun getAllPhotos() = db.getRoomDatabaseDao().getAllPhotos()
    suspend fun deletePhoto(photo: PhotoResponseItem) = db.getRoomDatabaseDao().deletePhoto(photo)

    // Todo
    suspend fun getTodos() = RetrofitInstance.api.getTodos()
    suspend fun upsertTodo(todo: TodoResponseItem) = db.getRoomDatabaseDao().upsertTodo(todo)
    fun getAllTodos() = db.getRoomDatabaseDao().getAllTodos()
    suspend fun deleteTodo(todo: TodoResponseItem) = db.getRoomDatabaseDao().deleteTodo(todo)

    // User
    suspend fun getUsers() = RetrofitInstance.api.getUsers()
    suspend fun upsertUser(user: UserResponseItem) = db.getRoomDatabaseDao().upsertUser(user)
    fun getAllUsers() = db.getRoomDatabaseDao().getAllUsers()
    suspend fun deleteUser(user: UserResponseItem) = db.getRoomDatabaseDao().deleteUser(user)

    fun getUserWithDetails(postId: Int) = db.getRoomDatabaseDao().getUserWithDetails(postId)

}