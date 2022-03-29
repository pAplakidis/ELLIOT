package com.example.elliot.di

import android.app.Application
import androidx.room.Room
import com.example.elliot.data.local.ElliotDatabase
import com.example.elliot.data.repository.FoodRepository
import com.example.elliot.data.repository.FoodRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFoodDatabase(app: Application): ElliotDatabase {
        return Room.databaseBuilder(
            app,
            ElliotDatabase::class.java,
            "elliot_database"
        )
            .createFromAsset("database/elliot.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodRepository(db: ElliotDatabase): FoodRepository {
        return FoodRepositoryImpl(db.dao)
    }
}