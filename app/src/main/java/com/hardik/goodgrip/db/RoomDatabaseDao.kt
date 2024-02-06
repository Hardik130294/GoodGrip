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
import com.hardik.goodgrip.models.CommentDetails
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.PostWithComments
import com.hardik.goodgrip.models.TodoDetails
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponseItem
import com.hardik.goodgrip.models.UserWithDetails

@Dao
interface RoomDatabaseDao {

    // Post
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPost(post: PostResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPost(posts: List<PostResponseItem>): List<Long>

    @Query("SELECT * FROM post")
    fun getAllPosts(): LiveData<List<PostResponseItem>>

    @Query("SELECT * FROM post WHERE user_id = :userId")
    fun getAllPosts(userId: Int): LiveData<List<PostResponseItem>>

    @Delete
    suspend fun deletePost(post: PostResponseItem)


    // Post with Comments (comments)
    @Query("SELECT * FROM post WHERE id = :postId")
    fun getPostWithComments(postId: Int): LiveData<List<PostWithComments>>


    // Comment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComment(comment: CommentResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComment(comments: List<CommentResponseItem>): List<Long>

    @Query("SELECT * FROM comment")
    fun getAllComments(): LiveData<List<CommentResponseItem>>

    @Query("SELECT * FROM comment where post_id = :postId")
    fun getAllComments(postId: Int): LiveData<List<CommentResponseItem>>

    @Delete
    suspend fun deleteComment(comment: CommentResponseItem)


    // Album
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlbum(album: AlbumResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlbum(albums: List<AlbumResponseItem>): List<Long>

    @Query("SELECT * FROM album")
    fun getAllAlbums(): LiveData<List<AlbumResponseItem>>

    @Query("SELECT * FROM album WHERE user_id = :userId")
    fun getAllAlbums(userId: Int): LiveData<List<AlbumResponseItem>>


    @Delete
    suspend fun deleteAlbum(album: AlbumResponseItem)


    // Album with Photos (Photos)
    @Query("SELECT * FROM album WHERE id = :albumId")
    fun getAlbumWithPhotos(albumId: Int): LiveData<List<AlbumWithPhotos>>


    // Photo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(photo: PhotoResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(photos: List<PhotoResponseItem>): List<Long>

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): LiveData<List<PhotoResponseItem>>

    @Query("SELECT * FROM photo where album_id = :albumId")
    fun getAllPhotos(albumId: Int): LiveData<List<PhotoResponseItem>>

    @Delete
    suspend fun deletePhoto(photo: PhotoResponseItem)


    // Todos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(todo: TodoResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(todo: List<TodoResponseItem>): List<Long>

    @Query("SELECT * FROM todo")
    fun getAllTodos(): LiveData<List<TodoResponseItem>>

    @Query("SELECT * FROM todo where user_id = :userId")
    fun getAllTodos(userId: Int): LiveData<List<TodoResponseItem>>

    @Delete
    suspend fun deleteTodo(todo: TodoResponseItem)

//    @Query("SELECT completed FROM todo WHERE id = :todoId")
//    fun isTodoCompleted(todoId: Int):  LiveData<Boolean>?
//
    @Query("UPDATE todo SET completed = :isCompleted WHERE id = :todoId")
    suspend fun updateTodoCompleted(todoId: Int, isCompleted: Boolean)

    // User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserResponseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(users: List<UserResponseItem>): List<Long>

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<UserResponseItem>>

    @Delete
    suspend fun deleteUser(user: UserResponseItem)

    // User with details (posts,albums,todos)
    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserWithDetails(userId: Int): LiveData<UserWithDetails>

    @Query(
        "SELECT " +
                "comment.id as commentId, " +
                "comment.name as commentName, " +
                "comment.email as commentEmail, " +
                "post.id as postId, " +
                "post.title as postTitle, " +
                "user.id as userId, " +
                "user.name as userName, " +
                "user.address_city as userCity, " +
                "user.email as userEmail " +
                "FROM comment " +
                "JOIN post ON comment.post_id = post.id " +
                "JOIN user ON post.user_id = user.id " +
                "WHERE comment.id = :commentId"
    )
    fun getCommentDetails(commentId: Int): LiveData<CommentDetails>

    @Query(
        "SELECT " +
                "todo.id as todoId, " +
                "todo.title as todoTitle, " +
                "todo.completed as completed, " +
                "user.id as userId, " +
                "user.username as userName " +
                "FROM todo JOIN user ON todo.user_id = user.id " +
                "WHERE todo.id = :todoId"
    )
    fun getTodoDetails(todoId: Int): LiveData<TodoDetails>

//    @Transaction
//    @Query("SELECT * FROM user")
//    suspend fun getUsersWithDetails(): LiveData<List<UserWithDetails>>

}