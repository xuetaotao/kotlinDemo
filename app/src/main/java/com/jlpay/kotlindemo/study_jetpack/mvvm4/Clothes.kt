package com.jlpay.kotlindemo.study_jetpack.mvvm4

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

/**
 * @ForeignKey ： 外键约束
 * 加了2个动作，在删除和更新的时候用了onDelete = CASCADE,onUpdate = CASCADE。这里动作有以下：
 * NO_ACTION：当person中的uid有变化的时候clothes的father_id不做任何动作
 * RESTRICT：当person中的uid在clothes里有依赖的时候禁止对person做动作，做动作就会报错。
 * SET_NULL：当person中的uid有变化的时候clothes的father_id会设置为NULL
 * SET_DEFAULT：当person中的uid有变化的时候clothes的father_id会设置为默认值，我这里是int型，那么会设置为0
 * CASCADE：当person中的uid有变化的时候clothes的father_id跟着变化，假如我把uid = 1的数据删除，那么clothes表里，father_id = 1的都会被删除
 */
@Entity(foreignKeys = [ForeignKey(onDelete = CASCADE,
    onUpdate = ForeignKey.CASCADE,
    entity = Person::class,
    parentColumns = arrayOf("uid"),
    childColumns = ["father_id"])])
class Clothes {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
    val color: String? = null
    val father_id: Int = 0
}