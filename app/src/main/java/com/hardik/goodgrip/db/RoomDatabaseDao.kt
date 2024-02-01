package com.hardik.goodgrip.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.AlbumWithPhotos
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.PostWithComments
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponseItem
import com.hardik.goodgrip.models.UserWithDetails

@Dao
interface RoomDatabaseDao {

    // Post
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPost(post: PostResponseItem) : Long

    @Query("SELECT * FROM post")
    fun getAllPosts() : LiveData<List<PostResponseItem>>

    @Delete
    suspend fun deletePost(post: PostResponseItem)


    // Post with Comments (comments)
    @Query("SELECT * FROM post WHERE id = :postId")
    fun getPostWithComments(postId: Int): LiveData<List<PostWithComments>>


    // Comment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComment(comment: CommentResponseItem) : Long

    @Query("SELECT * FROM comment")
    fun getAllComments() : LiveData<List<CommentResponseItem>>

    @Delete
    suspend fun deleteComment(comment: CommentResponseItem)


    // Album
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlbum(album: AlbumResponseItem) : Long

    @Query("SELECT * FROM album")
    fun getAllAlbums() : LiveData<List<AlbumResponseItem>>

    @Delete
    suspend fun deleteAlbum(album: AlbumResponseItem)


    // Album with Photos (Photos)
    @Query("SELECT * FROM album WHERE id = :albumId")
    fun getAlbumWithPhotos(albumId: Int): LiveData<List<AlbumWithPhotos>>


    // Photo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(photo: PhotoResponseItem) : Long

    @Query("SELECT * FROM photo")
    fun getAllPhotos() : LiveData<List<PhotoResponseItem>>

    @Delete
    suspend fun deletePhoto(photo: PhotoResponseItem)


    // Todos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(todo: TodoResponseItem) : Long

    @Query("SELECT * FROM todo")
    fun getAllTodos() : LiveData<List<TodoResponseItem>>

    @Delete
    suspend fun deleteTodo(todo: TodoResponseItem)

    // User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserResponseItem) : Long

    @Query("SELECT * FROM user")
    fun getAllUsers() : LiveData<List<UserResponseItem>>

    @Delete
    suspend fun deleteUser(user: UserResponseItem)

    // User with details (posts,albums,todos)
    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserWithDetails(userId: Int): LiveData<UserWithDetails>

//    @Transaction
//    @Query("SELECT * FROM user")
//    suspend fun getUsersWithDetails(): LiveData<List<UserWithDetails>>

}