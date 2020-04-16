package com.rhyzue.motion.data

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM task WHERE date_assigned = (:date)")
    fun getTaskByDate(date: Date): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = (:complete)")
    fun getTaskByCompletion(complete: Boolean): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

}

class TaskRepository(private val taskDao: TaskDao) {

    fun getTaskByDate(date: Date) {
        taskDao.getTaskByDate(date)
    }

    fun getTaskByCompletion(complete: Boolean) {
        taskDao.getTaskByCompletion(complete)
    }

    suspend fun insert(task: Task){
        taskDao.insert(task)
    }
}