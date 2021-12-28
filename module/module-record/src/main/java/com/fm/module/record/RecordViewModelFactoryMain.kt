package com.fm.module.record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordViewModelFactoryMain(
    private val repository: RecordRepositoryMain
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecordViewModelMain(repository) as T
    }

}