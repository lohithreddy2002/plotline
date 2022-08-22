package com.example.plotline

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val dao: ImagesDao) : ViewModel() {

    var originalImageUri: Uri? = null
    var edgedImageUri: Uri? = null

    private val _dataAddStatus = MutableStateFlow(false)
    val dataAddStatus = _dataAddStatus.asStateFlow()

    fun addImageToDatabase() {
        viewModelScope.launch {
            Log.e("orginal", "orginal $originalImageUri + edged $edgedImageUri")
            dao.addImageData(
                ImageData(
                    originalImage = originalImageUri.toString(),
                    edgedImage = edgedImageUri.toString()
                )
            )
            _dataAddStatus.value = true
        }
    }

    fun getImages(): Flow<List<ImageData>> {
        return dao.getImages()
    }
}