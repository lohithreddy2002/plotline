package com.example.plotline

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): PlotlineDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            PlotlineDatabase::class.java,
            "db"
        ).build()

    @Provides
    @Singleton
    fun provideImagesDao(database: PlotlineDatabase): ImagesDao =
        database.imagesDao()
}