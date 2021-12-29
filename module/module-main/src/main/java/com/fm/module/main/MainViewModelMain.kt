package com.fm.module.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fm.library.face.FaceSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModelMain(
    val repository: MainRepositoryMain
) : ViewModel() {

    fun loadFace() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }


}