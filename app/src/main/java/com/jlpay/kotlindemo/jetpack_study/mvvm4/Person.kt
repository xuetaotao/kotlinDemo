package com.jlpay.kotlindemo.jetpack_study.mvvm4

import androidx.room.*

/**
 * android Studio里查看db文件方法：
 * https://blog.csdn.net/qq_34783437/article/details/80770663
 *
 * Bean：实体类，表示数据库表的数据
 * 意思就是我们要往数据库里建表、建字段，就是使用这个bean对象。
 * 为了能保存数据，使数据持久化且Room必须能够对它进行操作，你可以用public修饰属性，或者你也可以设置成private,但必须提供set和get方法
 *
 * 首先介绍下注解
 * @Entity ： 数据表的实体类
 * @Entity(tableName = "person") 这样可以自定义表名，如果不设置则默认类名为表名
 * @Entity(primaryKeys = {"name","age"})，联合主键（不恰当的比喻）：如果一张表以人名为主键，很可能有同名覆盖掉数据，所以这个时候，我们用如果用人名和年龄，或者是人名和出生日期联合起来当主键，那就是唯一的。
 *
 * @PrimaryKey ： 每一个实体类都需要一个唯一的标识，主键
 * @ColumnInfo ： 数据表中字段名称
 * @Ignore ： 标注不需要添加到数据表中的属性
 * @Embedded ： 实体类中引用其他实体类
 * @ForeignKey ： 外键约束
 *
 */
@Entity
class Person {

    var name: String
    var age: Int = 0

    //@PrimaryKey：每一个实体类都需要唯一的表示，true表示自增长
    //@ColumnInfo：数据表中字段名称。如果不设置则默认为属性名
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Int = 0

    //@Ignore：标注不需要添加到数据表中的属性
    @Ignore
    var money: Int = 0

    //@Embedded 实体类中引用其他实体类。这样的话Address里属性也成为了表字段
    //@Embedded(prefix = "one"),这个是区分唯一性的，比如说一这个人可能有2个地址类似于tag，那么在数据表中就会以prefix+属性值命名
    @Embedded
    var address: Address? = null

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }
}