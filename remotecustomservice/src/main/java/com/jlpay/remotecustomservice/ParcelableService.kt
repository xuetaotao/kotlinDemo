package com.jlpay.remotecustomservice

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Kotlin中init代码块和构造方法以及伴生对象中代码的调用时机及执行顺序:https://blog.csdn.net/yuzhiqiang_1993/article/details/87863589
 */
class ParcelableService : Service() {

    private lateinit var petBinder: PetBinder

    companion object {

        private val pets: HashMap<Person, MutableList<Pet>> = HashMap()

        init {
            //初始化
            val list1: MutableList<Pet> = ArrayList<Pet>()
            list1.add(Pet("旺财", 4.3))
            list1.add(Pet("来福", 5.1))
            pets[Person(1, "sun", "sun")] = list1

            val list2: MutableList<Pet> = ArrayList<Pet>()
            list2.add(Pet("Kitty", 2.3))
            list2.add(Pet("garfield", 3.1))
            pets.put(Person(2, "bai", "bai"), list2)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return petBinder
    }

    override fun onCreate() {
        super.onCreate()
        petBinder = PetBinder()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class PetBinder : IPet.Stub() {

        override fun getPets(owner: Person?): MutableList<Pet>? {
            return pets[owner]
        }
    }
}