package com.example.gepst

import android.app.Application
import android.content.ClipData
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.logging.Filter

class ItemViewModel() : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Bitmap>()
    val selectedItem: LiveData<Bitmap> get() = mutableSelectedItem
    val filters = MutableLiveData<Set<Filter>>()

    fun selectItem(bitmap: Bitmap) {
        mutableSelectedItem.value = bitmap
    }

}