package com.rhyzue.motion.data

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("SELECT * FROM task WHERE date_assigned = (:date)")
    fun getTaskByDate(date: Date): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = (:complete)")
    fun getTaskByCompletion(complete: Boolean): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun modifyTask(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

}

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    fun getAllTasks(){
        taskDao.getAllTasks()
    }

    fun getTaskByDate(date: Date) {
        taskDao.getTaskByDate(date)
    }

    fun getTaskByCompletion(complete: Boolean) {
        taskDao.getTaskByCompletion(complete)
    }

    suspend fun getTaskById(id: Int): Task{
        return taskDao.getTaskById(id)
    }

    fun deleteAll(){
        taskDao.deleteAll()
    }

    fun insert(task: Task){
        taskDao.insert(task)
    }

    fun modifyTask(task: Task){
        taskDao.modifyTask(task)
    }

}