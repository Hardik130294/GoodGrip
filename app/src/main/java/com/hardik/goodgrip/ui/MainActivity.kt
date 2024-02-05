package com.hardik.goodgrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.db.RoomDatabaseInstance
import com.hardik.goodgrip.repository.RepositoryInstance
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    lateinit var viewModel: MainViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navView: BottomNavigationView
    lateinit var my_toolbar: Toolbar
    lateinit var appBarLayout: AppBarLayout
    lateinit var container: ConstraintLayout
    var isToolbarVisible = true
    var isBottomNavigationViewVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repositoryInstance = RepositoryInstance(RoomDatabaseInstance(this))
        val viewModelProviderFactory = MainViewModelProviderFactory(application, repositoryInstance)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)
        appBarLayout = findViewById(R.id.appBarLayout)
        my_toolbar = findViewById(R.id.my_toolbar)
        container = findViewById(R.id.container)
        setSupportActionBar(my_toolbar)

        navView = findViewById(R.id.nav_view)
//        navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_news))
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userFragment,R.id.userDetailsFragment,R.id.albumFragment,R.id.photoFragment,R.id.todoFragment,R.id.photoFragment,R.id.commentFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle the scroll change to show/hide the Toolbar and BottomNavigationView
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

            if (percentage >= 0.7f && isToolbarVisible) {
                // Toolbar is fully collapsed, hide it
                hideToolbarAndBottomNavigationView()
            } else if (percentage < 0.7f && !isToolbarVisible) {
                // Toolbar is not fully collapsed, show it
                showToolbarAndBottomNavigationView()
            }
        })
      /*  viewModel.posts.observe(this) {
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
        }*/
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun hideToolbarAndBottomNavigationView() {
        my_toolbar.animate().translationY(-my_toolbar.height.toFloat()).setDuration(200).start()
        navView.animate().translationY(navView.height.toFloat()).setDuration(200).start()
        isToolbarVisible = false
        isBottomNavigationViewVisible = false
    }

    private fun showToolbarAndBottomNavigationView() {
        my_toolbar.animate().translationY(0f).setDuration(200).start()
        navView.animate().translationY(0f).setDuration(200).start()
        isToolbarVisible = true
        isBottomNavigationViewVisible = true
    }


}

//https://jsonplaceholder.typicode.com/posts     -> one to many (user to posts)
//https://jsonplaceholder.typicode.com/comments  -> one to many (post to comments)
//https://jsonplaceholder.typicode.com/albums    -> one to many (user to albums)
//https://jsonplaceholder.typicode.com/photos    -> one to many (album to photos)
//https://jsonplaceholder.typicode.com/todos     ->
//https://jsonplaceholder.typicode.com/users     ->

