package com.wxsoft.fcare.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.local.dao.PatientDao


@Database(entities = [Patient::class], version = 1,exportSchema = true)
abstract class AppDatabase:RoomDatabase() {
    abstract fun patientDao(): PatientDao
}