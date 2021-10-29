package com.example.mvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvm.db.dao.ReadLaterDao
import com.example.mvvm.db.dao.ReadRecordDao
import com.example.mvvm.db.model.ReadLaterModel
import com.example.mvvm.db.model.ReadRecordModel

@Database(
    entities = [ReadLaterModel::class, ReadRecordModel::class],
    version = 1,
    exportSchema = false
)

abstract class WanRoom : RoomDatabase() {
    abstract fun readLaterDao(): ReadLaterDao
    abstract fun readRecordDao(): ReadRecordDao
}