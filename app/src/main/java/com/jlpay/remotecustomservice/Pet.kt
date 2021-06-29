package com.jlpay.remotecustomservice

import android.os.Parcel
import android.os.Parcelable

class Pet() : Parcelable {

    var name: String? = null
    var year: Double = 0.0

    constructor(name: String?, year: Double) : this() {
        this.name = name
        this.year = year
    }

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        year = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pet> {
        override fun createFromParcel(parcel: Parcel): Pet {
            return Pet(parcel)
        }

        override fun newArray(size: Int): Array<Pet?> {
            return arrayOfNulls(size)
        }
    }
}