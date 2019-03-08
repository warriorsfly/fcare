package com.wxsoft.fcare.core.domain.repository.patients

import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.domain.repository.Listing

interface IPatientRepository {
    fun getPatients(item: PatientsCondition): Listing<Patient>
}