package com.hardik.goodgrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.hardik.goodgrip.R
import com.hardik.goodgrip.db.RoomDatabaseInstance
import com.hardik.goodgrip.repository.RepositoryInstance
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repositoryInstance = RepositoryInstance(RoomDatabaseInstance(this))
        val viewModelProviderFactory = MainViewModelProviderFactory(application, repositoryInstance)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        viewModel.posts.observe(this) {
            it.data?.iterator()?.forEach { post ->
                viewModel.savePost(post)
            }
        }
        viewModel.getSavedPost().observe(this) {
            it?.iterator()?.forEach { post ->
//                Log.w(TAG, "onCreate: $post")
            }
        }

        viewModel.comments.observe(this) {
            it.data?.iterator()?.forEach { comment ->
                viewModel.saveComment(comment)
            }
        }
        viewModel.getSavedComments().observe(this) {
            it?.iterator()?.forEach { comment ->
//                Log.e(TAG, "onCreate: $comment")
            }
        }

        viewModel.getSavedPostWithComments().observe(this) {
            it?.iterator()?.forEach { postWithComment ->
//                Log.e(TAG, "onCreate: \n$postWithComment")
            }
        }

        viewModel.albums.observe(this) {
            it.data?.iterator()?.forEach { album ->
                viewModel.saveAlbum(album)
            }
        }
        viewModel.getSavedAlbums().observe(this) {
            it?.iterator()?.forEach { album ->
//                Log.e(TAG, "onCreate: $album")
            }
        }

        viewModel.photos.observe(this) {
            it.data?.iterator()?.forEach { photo ->
                viewModel.savePhoto(photo)
            }
        }
        viewModel.getSavedPhotos().observe(this) {
            it?.iterator()?.forEach { photo ->
//                Log.e(TAG, "onCreate: $photo")
            }
        }

        viewModel.todos.observe(this) {
            it.data?.iterator()?.forEach { todo ->
                viewModel.saveTodo(todo)
            }
        }
        viewModel.getSavedTodos().observe(this) {
            it?.iterator()?.forEach { todo ->
//                Log.e(TAG, "onCreate: $todo")
            }
        }

        viewModel.users.observe(this) {
            it.data?.iterator()?.forEach { user ->
                viewModel.saveUser(user)
            }
        }
        viewModel.getSavedUsers().observe(this) {
            it?.iterator()?.forEach { user ->
//                Log.e(TAG, "onCreate: $user")
            }
        }


        repositoryInstance.getPostWithComments(1).observe(this) {
            it?.iterator()?.forEach { postWithComments ->
                Log.e(TAG, "post: \n${postWithComments.post}\n")
                postWithComments.comments.iterator().forEach { comment ->
                    Log.i(TAG, "comment: \n$comment\n\n")
                }
            }
        }

        repositoryInstance.getUserWithDetails(1).observe(this){
            it?.let {userWithDetails->
                Log.w(TAG,
                    "UserWithDetails:->\n" +
                        "${userWithDetails.user.id}, \n" +
                        "${userWithDetails.user.name}, \n" +
                        "${userWithDetails.user.username}, \n" +
                        "${userWithDetails.user.email}, \n" +
                        "${userWithDetails.user.phone}, \n" +
                        "${userWithDetails.user.website}, \n" +
                        "Address:->\n" +
                        "${userWithDetails.user.address.city}, \n" +
                        "${userWithDetails.user.address.street}, \n" +
                        "${userWithDetails.user.address.suite}, \n" +
                        "${userWithDetails.user.address.zipcode}, \n" +
                        "GEO:->\n"+
                        "${userWithDetails.user.address.geo.lat}, \n" +
                        "${userWithDetails.user.address.geo.lng}, \n" +
                        "Company:->\n"+
                        "${userWithDetails.user.company.name}, \n"+
                        "${userWithDetails.user.company.bs}, \n"+
                        "${userWithDetails.user.company.catchPhrase},"
                )
                userWithDetails.albums.iterator().forEach {album ->
                    Log.w(TAG,
                        "Album:->\n" +
                            "${album.id}, \n" +
                            "${album.userId}, \n" +
                            "${album.title}, \n"
                    )
                }
                userWithDetails.posts.iterator().forEach { post ->
                    Log.d(TAG, "Post:->\n" +
                            "${post.id}, \n" +
                            "${post.userId}, \n" +
                            "${post.title}, \n" +
                            "${post.body}, \n"
                    )
                }
                userWithDetails.todos.iterator().forEach { todo ->
                    Log.d(TAG, "Todo:->\n" +
                            "${todo.id}, \n" +
                            "${todo.userId}, \n" +
                            "${todo.title}, \n" +
                            "${todo.completed}, \n"
                    )
                }

            }
        }

        repositoryInstance.getAlbumWithPhotos(1).observe(this){
            it?.iterator()?.forEach {albumWithphotos->
                Log.d(TAG, "Album:->\n" +
                        "${albumWithphotos.album.id}, \n"+
                        "${albumWithphotos.album.userId}, \n"+
                        "${albumWithphotos.album.title}, \n"
                )
                albumWithphotos.photos.iterator().forEach {photo ->
                    Log.d(TAG, "Photo:->\n" +
                            "${photo.id}, \n" +
                            "${photo.albumId}, \n" +
                            "${photo.title}, \n" +
                            "${photo.url}, \n" +
                            "${photo.thumbnailUrl},"
                    )
                }
            }
        }
    }
}

//https://jsonplaceholder.typicode.com/posts     -> one to many (user to posts)
//https://jsonplaceholder.typicode.com/comments  -> one to many (post to comments)
//https://jsonplaceholder.typicode.com/albums    -> one to many (user to albums)
//https://jsonplaceholder.typicode.com/photos    -> one to many (album to photos)
//https://jsonplaceholder.typicode.com/todos     ->
//https://jsonplaceholder.typicode.com/users     ->

