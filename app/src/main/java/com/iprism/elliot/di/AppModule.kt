package com.iprism.elliot.di

import android.app.Application
import androidx.room.Room
import com.iprism.elliot.data.local.ElliotDatabase
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.data.repository.FoodRepositoryImpl
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