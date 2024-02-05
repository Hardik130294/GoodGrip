package com.hardik.goodgrip.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hardik.goodgrip.ApplicationInstance
import com.hardik.goodgrip.models.AlbumResponse
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.CommentResponse
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.models.PhotoResponse
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.models.PostResponse
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.models.TodoResponse
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.models.UserResponse
import com.hardik.goodgrip.models.UserResponseItem
import com.hardik.goodgrip.repository.RepositoryInstance
import com.hardik.goodgrip.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(app: Application, private val repositoryInstance: RepositoryInstance) : AndroidViewModel(app) {
    val TAG = MainViewModel::class.java.simpleName

    val posts : MutableLiveData<Resource<PostResponse>> = MutableLiveData()
    var postResponse : PostResponse? = null

    val comments : MutableLiveData<Resource<CommentResponse>> = MutableLiveData()
    var commentResponse : CommentResponse? = null

    val albums : MutableLiveData<Resource<AlbumResponse>> = MutableLiveData()
    var albumResponse : AlbumResponse? = null

    val photos : MutableLiveData<Resource<PhotoResponse>> = MutableLiveData()
    var photoResponse : PhotoResponse? = null

    val todos : MutableLiveData<Resource<TodoResponse>> = MutableLiveData()
    var todoResponse : TodoResponse? = null

    val users: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    var userResponse : UserResponse? = null


    init {
//        getPosts()
//        getComments()
//        getAlbums()
//        getPhotos()
//        getTodos()
//        getUsers()
    }

    // Post API methods
    fun getPosts() = viewModelScope.launch {
        Log.d(TAG, "getPosts: viewModelScope")
        safePostCall()
    }
    private suspend fun safePostCall() {
        posts.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getPosts()
                posts.postValue(handlePostResponse(response))
            }else{
                posts.postValue(Resource.Error("No internet Connection"))
            }

        }catch (t : Throwable){
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
        }
    }
    private fun handlePostResponse(response: Response<PostResponse>): Resource<PostResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handlePostResponse: ")
                if (postResponse == null){
                    postResponse = resultResponse
                }else{
                    val oldData = postResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = postResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // Post DB request
    fun savePost(postResponseItem: PostResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertPost(postResponseItem)
    }
    fun getSavedPosts() = repositoryInstance.getAllPosts()
    fun getSavedPosts(userId: Int) = repositoryInstance.getAllPosts(userId = userId)
    fun deletePost(postResponseItem: PostResponseItem) = viewModelScope.launch {
        repositoryInstance.deletePosts(postResponseItem)
    }


    // Comment API methods
    private fun getComments() = viewModelScope.launch {
        Log.d(TAG, "getComments: viewModelScope")
        safeCommentCall()
    }
    private suspend fun safeCommentCall() {
        comments.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getComments()
                comments.postValue(handleCommentsResponse(response))
            }else{
                comments.postValue(Resource.Error("NoInternetConnection"))
            }

        }catch (t : Throwable) {
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
            Log.d(TAG, "safeCommentCall: ${t.localizedMessage}")
            comments.postValue(Resource.Error(t.localizedMessage))
        }
    }
    private fun handleCommentsResponse(response: Response<CommentResponse>): Resource<CommentResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handleCommentsResponse: ")
                if (commentResponse == null){
                    commentResponse = resultResponse
                }else{
                    val oldData = commentResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = commentResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // Comment DB request
    fun saveComment(commentResponseItem: CommentResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertComment(commentResponseItem)
    }
    fun getSavedComments() = repositoryInstance.getAllComments()
    fun getSavedComments(postId: Int) = repositoryInstance.getAllComments(postId = postId)
    fun deleteComments(commentResponseItem: CommentResponseItem) = viewModelScope.launch {
        Log.d(TAG, "deleteComments: viewModelScope")
        repositoryInstance.deleteComments(commentResponseItem)
    }

    // Post With Comment DB request
    fun getSavedPostWithComments(post_id:Int=1) = repositoryInstance.getPostWithComments(post_id)

    // Album API request
    fun getAlbums() = viewModelScope.launch {
        Log.d(TAG, "getAlbums: viewModelScope")
        safeAlbumCall()
    }
    private suspend fun safeAlbumCall() {
        albums.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getAlbums()
                albums.postValue(handleAlbumResponse(response))
            }else{
                albums.postValue(Resource.Error("NoInternetConnection"))
            }

        }catch (t : Throwable) {
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
            Log.d(TAG, "safeAlbumCall: ${t.localizedMessage}")
            albums.postValue(Resource.Error(t.localizedMessage))
        }
    }
    private fun handleAlbumResponse(response: Response<AlbumResponse>): Resource<AlbumResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handleAlbumResponse: ")
                if (albumResponse == null){
                    albumResponse = resultResponse
                }else{
                    val oldData = albumResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = albumResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // Album DB request
    fun saveAlbum(response: AlbumResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertAlbum(response)
    }
    fun getSavedAlbums() = repositoryInstance.getAllAlbums()
    fun getSavedAlbums(userId: Int) = repositoryInstance.getAllAlbums(userId = userId)
    fun deleteAlbum(response: AlbumResponseItem) = viewModelScope.launch {
        Log.d(TAG, "deleteAlbum: viewModelScope")
        repositoryInstance.deleteAlbum(response)
    }

    // Photo API request
    fun getPhotos() = viewModelScope.launch {
        Log.d(TAG, "getPhotos: viewModelScope")
        safePhotoCall()
    }
    private suspend fun safePhotoCall() {
        photos.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getPhotos()
                photos.postValue(handlePhotoResponse(response))
            }else{
                photos.postValue(Resource.Error("NoInternetConnection"))
            }

        }catch (t : Throwable) {
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
            Log.d(TAG, "safePhotoCall: ${t.localizedMessage}")
            photos.postValue(Resource.Error(t.localizedMessage))
        }
    }
    private fun handlePhotoResponse(response: Response<PhotoResponse>): Resource<PhotoResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handlePhotoResponse: ")
                if (photoResponse == null){
                    photoResponse = resultResponse
                }else{
                    val oldData = photoResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = photoResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // Photo DB request
    fun savePhoto(response: PhotoResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertPhoto(response)
    }
    fun getSavedPhotos() = repositoryInstance.getAllPhotos()
    fun getSavedPhotos(albumId: Int) = repositoryInstance.getAllPhotos(albumId = albumId)
    fun deletePhoto(response: PhotoResponseItem) = viewModelScope.launch {
        Log.d(TAG, "deletePhoto: viewModelScope")
        repositoryInstance.deletePhoto(response)
    }

    // Todo API request
    fun getTodos() = viewModelScope.launch {
        Log.d(TAG, "getTodos: viewModelScope")
        safeTodoCall()
    }
    private suspend fun safeTodoCall() {
        todos.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getTodos()
                todos.postValue(handleTodoResponse(response))
            }else{
                todos.postValue(Resource.Error("NoInternetConnection"))
            }

        }catch (t : Throwable) {
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
            Log.d(TAG, "safeTodoCall: ${t.localizedMessage}")
            todos.postValue(Resource.Error(t.localizedMessage))
        }
    }
    private fun handleTodoResponse(response: Response<TodoResponse>): Resource<TodoResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handleTodoResponse: ")
                if (todoResponse == null){
                    todoResponse = resultResponse
                }else{
                    val oldData = todoResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = todoResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // Todo DB request
    fun saveTodo(response: TodoResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertTodo(response)
    }
    fun getSavedTodos() = repositoryInstance.getAllTodos()
    fun getSavedTodos(userId: Int) = repositoryInstance.getAllTodos(userId = userId)
    fun deleteTodo(response: TodoResponseItem) = viewModelScope.launch {
        Log.d(TAG, "deleteTodo: viewModelScope")
        repositoryInstance.deleteTodo(response)
    }

    // User API request
    fun getUsers() = viewModelScope.launch {
        Log.d(TAG, "getUsers: viewModelScope")
        safeUserCall()
    }
    private suspend fun safeUserCall() {
        users.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = repositoryInstance.getUsers()
                users.postValue(handleUserResponse(response))
            }else{
                users.postValue(Resource.Error("NoInternetConnection"))
            }

        }catch (t : Throwable) {
            when(t){
                is IOException -> posts.postValue(Resource.Error("Network failure!!!"))
                else -> posts.postValue(Resource.Error("Conversion error!!!"))
            }
            Log.d(TAG, "safeUserCall: ${t.localizedMessage}")
            users.postValue(Resource.Error(t.localizedMessage))
        }
    }
    private fun handleUserResponse(response: Response<UserResponse>): Resource<UserResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                Log.d(TAG, "handleUserResponse: ")
                if (userResponse == null){
                    userResponse = resultResponse
                }else{
                    val oldData = userResponse
                    val newData = resultResponse
                    oldData?.addAll(newData)
                }
                return Resource.Success(data = userResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    // User DB request
    fun saveUser(response: UserResponseItem) = viewModelScope.launch {
        repositoryInstance.upsertUser(response)
    }
    fun getSavedUsers() = repositoryInstance.getAllUsers()
    fun deleteUser(response: UserResponseItem) = viewModelScope.launch {
        Log.d(TAG, "deleteUser: viewModelScope")
        repositoryInstance.deleteUser(response)
    }


    // Check internet connection
    @SuppressLint("ObsoleteSdkInt")
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ApplicationInstance>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}