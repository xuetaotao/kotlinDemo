package com.jlpay.kotlindemo.jetpack_study.mvvm4

import androidx.room.*

/**
 * Dao：数据操作类，包含用于访问数据库的方法
 *
 * @Dao ： 标注数据库操作的类
 * @Query ： 包含所有Sqlite语句操作
 * @Insert ： 标注数据库的插入操作
 * @Delete ： 标注数据库的删除操作
 * @Update ： 标注数据库的更新操作
 *
 * 对数据库设计时，不允许重复数据的出现。否则，必然造成大量的冗余数据。实际上，难免会碰到这个问题：冲突。当我们像数据库插入数据时，该数据已经存在了，必然造成了冲突。
 * 该冲突该怎么处理呢？
 *
 * 在@Insert注解中有conflict用于解决插入数据冲突的问题，其默认值为OnConflictStrategy.ABORT。对于OnConflictStrategy而言，它封装了Room解决冲突的相关策略
 * OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务
 * OnConflictStrategy.ROLLBACK：冲突策略是回滚事务
 * OnConflictStrategy.ABORT：冲突策略是终止事务
 * OnConflictStrategy.FAIL：冲突策略是事务失败
 * OnConflictStrategy.IGNORE：冲突策略是忽略冲突
 * 这里比如在插入的时候我们加上了OnConflictStrategy.REPLACE，那么往已经有uid=1的person表里再插入uid =1的person数据，那么新数据会覆盖就数据。
 * 如果我们什么都不加，那么久是默认的OnConflictStrategy.ABORT，重复上面的动作，你会发现，程序崩溃了。也就是上面说的终止事务
 */
@Dao
interface PersonDao {

    //一次插入单条数据 或 多条
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    fun insert(persons: Array<Person>)
//    fun insert(vararg persons: Person)//同上，等价于Java的 String... args 或者 String[] args

    //一次更新单条数据 或 多条
    @Update
    fun update(vararg persons: Person)

    //一次删除单条数据 或 多条
    @Delete
    fun delete(persons: Array<Person>)

    //查询所有数据
    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    //删除全部数据
    @Query("DELETE FROM person")
    fun deleteAll()

    //根据字段去查找数据
    @Query("SELECT * FROM person WHERE uid=:uid")
    fun getPersonByUid(uid: Int): Person

    //一次查找多个数据
    @Query("SELECT * FROM person WHERE uid IN(:userIds)")
    fun loadAllByIds(userIds: List<Int>)

    //多个条件查找
    @Query("SELECT * FROM person WHERE name=:name AND age=:age")
    fun getPersonByNameAge(name: String, age: Int)
}