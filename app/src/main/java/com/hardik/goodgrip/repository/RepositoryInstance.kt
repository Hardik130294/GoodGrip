package com.hardik.goodgrip.repository

import com.hardik.goodgrip.api.RetrofitInstance
import com.hardik.goodgrip.db.RoomDatabaseInstance
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponseItem

class RepositoryInstance (val db : RoomDatabaseInstance){
    val TAG = RepositoryInstance::class.java.simpleName

    // Post
    suspend fun getPosts() = RetrofitInstance.api.getPosts()

    suspend fun upsertPost(post: PostResponseItem) = db.getRoomDatabaseDao().upsertPost(post)
    suspend fun upsertPost(posts: List<PostResponseItem>) = db.getRoomDatabaseDao().upsertPost(posts)
    fun getAllPosts() = db.getRoomDatabaseDao().getAllPosts()
    fun getAllPosts(userId: Int) = db.getRoomDatabaseDao().getAllPosts(userId = userId)
    suspend fun deletePosts(post: PostResponseItem) = db.getRoomDatabaseDao().deletePost(post)

    // Post with Comments
    fun getPostWithComments(postId: Int) = db.getRoomDatabaseDao().getPostWithComments(postId)

    // Comment
    suspend fun getComments() = RetrofitInstance.api.getComments()

    suspend fun upsertComment(comment: CommentResponseItem) = db.getRoomDatabaseDao().upsertComment(comment)
    suspend fun upsertComment(comments: List<CommentResponseItem>) = db.getRoomDatabaseDao().upsertComment(comments)
    fun getAllComments() = db.getRoomDatabaseDao().getAllComments()
    fun getAllComments(postId: Int) = db.getRoomDatabaseDao().getAllComments(postId = postId)

    suspend fun deleteComments(comment: CommentResponseItem) = db.getRoomDatabaseDao().deleteComment(comment)

    // Album
    suspend fun getAlbums() = RetrofitInstance.api.getAlbums()
    suspend fun upsertAlbum(album: AlbumResponseItem) = db.getRoomDatabaseDao().upsertAlbum(album)
    suspend fun upsertAlbum(albums: List<AlbumResponseItem>) = db.getRoomDatabaseDao().upsertAlbum(albums)
    fun getAllAlbums() = db.getRoomDatabaseDao().getAllAlbums()
    fun getAllAlbums(userId: Int) = db.getRoomDatabaseDao().getAllAlbums(userId = userId)
    suspend fun deleteAlbum(album: AlbumResponseItem) = db.getRoomDatabaseDao().deleteAlbum(album)

    // Album with Photos
    fun getAlbumWithPhotos(albumId: Int) = db.getRoomDatabaseDao().getAlbumWithPhotos(albumId)

    // Photo
    suspend fun getPhotos() = RetrofitInstance.api.getPhotos()
    suspend fun upsertPhoto(photo: PhotoResponseItem) = db.getRoomDatabaseDao().upsertPhoto(photo)
    suspend fun upsertPhoto(photos: List<PhotoResponseItem>) = db.getRoomDatabaseDao().upsertPhoto(photos)
    fun getAllPhotos() = db.getRoomDatabaseDao().getAllPhotos()
    fun getAllPhotos(albumId: Int) = db.getRoomDatabaseDao().getAllPhotos(albumId = albumId)
    suspend fun deletePhoto(photo: PhotoResponseItem) = db.getRoomDatabaseDao().deletePhoto(photo)

    // Todo
    suspend fun getTodos() = RetrofitInstance.api.getTodos()
    suspend fun upsertTodo(todo: TodoResponseItem) = db.getRoomDatabaseDao().upsertTodo(todo)
    suspend fun upsertTodo(todos: List<TodoResponseItem>) = db.getRoomDatabaseDao().upsertTodo(todos)
    fun getAllTodos() = db.getRoomDatabaseDao().getAllTodos()
    fun getAllTodos(userId: Int) = db.getRoomDatabaseDao().getAllTodos(userId = userId)
    suspend fun deleteTodo(todo: TodoResponseItem) = db.getRoomDatabaseDao().deleteTodo(todo)
//    fun isTodoCompleted(todoId: Int) = db.getRoomDatabaseDao().isTodoCompleted(todoId = todoId)
    suspend fun updateTodoCompleted(todoId: Int, isCompleted: Boolean) = db.getRoomDatabaseDao().updateTodoCompleted(todoId = todoId, isCompleted = isCompleted)

    // User
    suspend fun getUsers() = RetrofitInstance.api.getUsers()
    suspend fun upsertUser(user: UserResponseItem) = db.getRoomDatabaseDao().upsertUser(user)
    suspend fun upsertUser(users: List<UserResponseItem>) = db.getRoomDatabaseDao().upsertUser(users)
    fun getAllUsers() = db.getRoomDatabaseDao().getAllUsers()
    suspend fun deleteUser(user: UserResponseItem) = db.getRoomDatabaseDao().deleteUser(user)

    fun getUserWithDetails(postId: Int) = db.getRoomDatabaseDao().getUserWithDetails(postId)

    fun getCommentDetails(commentId: Int) = db.getRoomDatabaseDao().getCommentDetails(commentId = commentId)

    fun getTodoDetails(todoId: Int) = db.getRoomDatabaseDao().getTodoDetails(todoId = todoId)

}