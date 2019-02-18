package com.wxsoft.fcare.core.di

import androidx.room.Room
import android.content.Context
import com.wxsoft.fcare.core.data.local.AppDatabase
import com.wxsoft.fcare.core.data.local.dao.PatientDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDatabase =
        Room.databaseBuilder(context,AppDatabase::class.java, "db-f-care").build()

    @Provides
    fun providePatientDao(db:AppDatabase): PatientDao {
        return db.patientDao()
    }
}