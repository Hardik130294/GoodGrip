package com.hardik.goodgrip.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardik.goodgrip.repository.RepositoryInstance

class MainViewModelProviderFactory(private val app: Application, private val repositoryInstance: RepositoryInstance) : ViewModelProvider.Factory{
    val TAG = MainViewModelProviderFactory::class.java.simpleName

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        Log.d(TAG, "create: ")
        return MainViewModel(app, repositoryInstance) as T
    }
}