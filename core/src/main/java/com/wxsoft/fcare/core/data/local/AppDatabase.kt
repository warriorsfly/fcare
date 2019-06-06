package com.wxsoft.fcare.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.local.dao.PatientDao


@Database(entities = [Patient::class], version = 2,exportSchema = true)
abstract class AppDatabase:RoomDatabase() {
    abstract fun patientDao(): PatientDao
}