package com.iprism.elliot.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.iprism.elliot.data.local.ElliotDatabase
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.data.repository.FoodRepositoryImpl
import com.iprism.elliot.domain.rules.Ruleset
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideFoodRepository(db: ElliotDatabase, app: Application): FoodRepository {
        return FoodRepositoryImpl(db.dao, app.resources)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRuleset(@ApplicationContext context: Context): Ruleset {
        return Ruleset(context)
    }
}