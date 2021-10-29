package com.example.mvvm.db.repository

import com.example.mvvm.constant.Constant
import com.example.mvvm.db.WanDb.db
import com.example.mvvm.db.model.ReadRecordModel

class ReadRecordRepository : BaseDBRepository() {

    fun add(link: String, title: String, success: ((t: Unit) -> Unit)? = null) {
        execute({
            val model = ReadRecordModel(link, title, System.currentTimeMillis())
            db().readRecordDao().insert(model)
        }, success)
    }

    fun remove(link: String, success: ((t: Unit) -> Unit)? = null) {
        execute({
            db().readRecordDao().delete(link)
        }, success)
    }

    fun removeAll(success: ((t: Unit) -> Unit)? = null) {
        execute({
            db().readRecordDao().deleteAll()
        }, success)
    }

    fun getList(
        from: Int,
        count: Int,
        success: ((t: List<ReadRecordModel>) -> Unit)? = null
    ) {
        execute({
            db().readRecordDao().findAll(from, count)
        }, success)
    }

    fun removeIfMaxCount(success: ((t: Unit) -> Unit)) {
        execute({
            db().readRecordDao().deleteIfMaxCount(Constant.READ_RECORD_MAX_COUNT)
        }, success)
    }
}