package com.wxsoft.fcare.core.data.local.dao

import androidx.room.*
import com.wxsoft.fcare.core.data.entity.Patient
import io.reactivex.Flowable

@Dao
interface PatientDao{
    @Query("SELECT * FROM patients order by createdDate desc")
    fun getAll(): Flowable<List<Patient>>

    @Query("SELECT * FROM patients where id =:id")
    fun getOne(id:String): Flowable<Patient>

    @Query("SELECT * FROM patients WHERE id IN (:patientIds)")
    fun loadAllByIds(patientIds: IntArray): List<Patient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg patients: Patient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(patients: List<Patient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(patient: Patient)

    @Delete
    fun delete(patient: Patient)
}