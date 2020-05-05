package com.rhyzue.motion.ui.schedule.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rhyzue.motion.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel(application: Application) : AndroidViewModel(application){
    private val taskRepo: TaskRepository
    private val typeRepo: TypeRepository


    val allTasks: LiveData<List<Task>>
    val allTypes: LiveData<List<Type>>

    init {
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        val typeDao = AppDatabase.getDatabase(application, viewModelScope).typeDao()

        taskRepo = TaskRepository(taskDao)
        typeRepo = TypeRepository(typeDao)

        allTasks = taskRepo.allTasks
        allTypes = typeRepo.allTypes
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val pr = taskRepo.insert(task)
        println(pr)
    }

    fun insertType(type: Type) = viewModelScope.launch(Dispatchers.IO) {
        val pr = typeRepo.insert(type)
        println(pr)
    }

    fun getTaskById(id: Int): Task{
        return taskRepo.getTaskById(id)
    }

    fun modifyTask(task: Task){
        taskRepo.modifyTask(task)
    }

}
