package com.jlpay.kotlindemo.study_jetpack.mvvm4

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database：数据库持有者 & 数据库版本管理者
 * 注解指定了database的表映射实体数据以及版本等信息
 */
@Database(entities = [Person::class, Clothes::class, Dog::class], version = 1)
abstract class DataBase : RoomDatabase() {

    abstract fun getPersonDao(): PersonDao
    abstract fun getClothesDao(): ClothesDao
    abstract fun getDogDao(): DogDao

    //数据库变动添加Migration(迁移)
    companion object {
        @JvmStatic
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //告诉person表，增添一个String类型的字段 son
                database.execSQL("ALTER TABLE person ADD COLUMN son TEXT")
            }
        }
    }
}