package com.jlpay.remotecustomservice

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Person() : Parcelable {

    var id: Int = 0
    var name: String? = null
    var password: String? = null

    constructor(id: Int, name: String?, password: String?) : this() {
        this.id = id
        this.name = name
        this.password = password
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        password = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        //把该对象所包含的数据写到Parcel中
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    //负责从Parcel数据包中恢复Person对象
    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        //kotlin中==比较的是数值是否相等, 而===比较的是两个对象的地址是否相等
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val person: Person = other as Person
        return Objects.equals(name, person.name) && Objects.equals(password, person.password)
    }

    override fun hashCode(): Int {
        return Objects.hash(name, password)
    }
}