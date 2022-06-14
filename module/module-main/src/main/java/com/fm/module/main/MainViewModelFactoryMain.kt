package com.fm.module.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactoryMain(
    private val context: Context,
    private val repository: MainRepositoryMain
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModelMain(context, repository) as T
    }

}