package com.example.plotline

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageData::class], version = 1)
abstract class PlotlineDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
}