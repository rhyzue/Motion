package com.rhyzue.motion.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.apache.commons.lang3.time.DateUtils
import java.util.*

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val type: Int,
    @ColumnInfo val date_assigned: Date,
    @ColumnInfo val complete: Boolean,
    @ColumnInfo val deadline: Date?,
    @ColumnInfo val goal_id: Int
)

@Dao
interface TaskDao{

    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Int): Task

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun modifyTask(task: Task)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun removeTask(id: Int)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Query("SELECT * FROM task WHERE date_assigned BETWEEN :startDate AND :endDate")
    fun getTaskByDate(startDate: Date, endDate: Date): List<Task>

}

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int): Task{
        return taskDao.getTaskById(id)
    }

    fun insert(task: Task){
        taskDao.insert(task)
    }

    suspend fun modifyTask(task: Task){
        taskDao.modifyTask(task)
    }

    suspend fun removeTask(id: Int){
        taskDao.removeTask(id)
    }

    fun getTaskByDate(date: Date): List<Task>{
        val ed = DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1)
        val sd = DateUtils.truncate(date, Calendar.DATE)

        return taskDao.getTaskByDate(sd, ed)
    }

}