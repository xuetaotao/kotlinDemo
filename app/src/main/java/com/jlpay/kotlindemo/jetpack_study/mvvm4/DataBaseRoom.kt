package com.jlpay.kotlindemo.jetpack_study.mvvm4

import androidx.room.Room
import com.jlpay.kotlindemo.utils.AppUtils

/**
 * Room：数据库的创建者 & 负责数据库版本更新的具体实现者
 */
class DataBaseRoom {

    companion object {
        @JvmStatic
        private val DB_NAME: String = "room_test"

        @JvmStatic
        private var dataBase: DataBase? = null

        @JvmStatic
        fun getDataBaseInstance(): DataBase? {
            if (dataBase == null) {
                synchronized(DataBaseRoom::class.java) {
                    if (dataBase == null) {
                        dataBase = Room.databaseBuilder(AppUtils.getApplication(),
                            DataBase::class.java,
                            DB_NAME)
//                            .allowMainThreadQueries()//允许主线程查询，不推荐打开这句
//                            .addMigrations(DataBase.MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return dataBase
        }
    }
}