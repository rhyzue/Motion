package com.rhyzue.motion.dataClasses

import androidx.room.*
import java.util.*

@Entity
data class Task(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val type: Int?,
    @ColumnInfo val date_assigned: Date,
    @ColumnInfo val complete: Boolean,
    @ColumnInfo val deadline: Date?,
    @ColumnInfo val auto_push: Boolean,
    @ColumnInfo val goal_id: Int?
)

@Dao
interface TaskDao{
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE date_assigned = (:date)")
    fun getTaskByDate(date: Date): List<Task>

    @Query("SELECT * FROM task WHERE complete = (:complete)")
    fun getTaskByCompletion(complete: Boolean): List<Task>
}