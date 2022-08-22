package com.example.plotline

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0,
    val originalImage:String,
    val edgedImage:String

)