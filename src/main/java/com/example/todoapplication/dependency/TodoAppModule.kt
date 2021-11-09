package com.example.todoapplication.dependency

import android.app.Application
import androidx.room.Room
import com.example.todoapplication.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoAppModule {
    @Provides
    @Singleton
    fun provideDatabase(app:Application) =
        Room.databaseBuilder(app,TaskDatabase::class.java,"task_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db:TaskDatabase) = db.taskDao()
}