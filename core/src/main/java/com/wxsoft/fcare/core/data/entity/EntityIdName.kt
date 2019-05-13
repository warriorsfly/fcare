package com.wxsoft.fcare.core.data.entity

import android.os.Parcel
import android.os.Parcelable

data class EntityIdName(var id:String,
               var name:String): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityIdName> {
        override fun createFromParcel(parcel: Parcel): EntityIdName {
            return EntityIdName(parcel)
        }

        override fun newArray(size: Int): Array<EntityIdName?> {
            return arrayOfNulls(size)
        }
    }

}