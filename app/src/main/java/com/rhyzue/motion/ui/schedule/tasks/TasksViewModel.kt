package com.rhyzue.motion.ui.schedule.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rhyzue.motion.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class TasksViewModel(application: Application) : AndroidViewModel(application){
    private val taskRepo: TaskRepository
    private val typeRepo: TypeRepository


    val todayTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }
    val allTypes: LiveData<List<Type>>


    init {
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        val typeDao = AppDatabase.getDatabase(application, viewModelScope).typeDao()

        taskRepo = TaskRepository(taskDao)
        typeRepo = TypeRepository(typeDao)

        allTypes = typeRepo.allTypes

    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val newId = taskRepo.insert(task)
        val newTask: Task = taskRepo.getTaskById(newId.toInt())
        val mutableList = mutableListOf<Task>()
        todayTasks.value?.let {
            mutableList.addAll(it)
            mutableList.add(newTask)
        }
        todayTasks.postValue(mutableList.toList())
    }

    fun getTaskById(id: Int): Task {
        return runBlocking{
            taskRepo.getTaskById(id)
        }
    }

    fun modifyTask(task: Task)= viewModelScope.launch(Dispatchers.IO) {
        taskRepo.modifyTask(task)
        val mutableList = mutableListOf<Task>()
        todayTasks.value?.let {
            for(i in todayTasks.value!!){
                if(i.id==task.id)
                    mutableList.add(task)
                else
                    mutableList.add(i)
            }
        }

        todayTasks.postValue(mutableList.toList())
    }

    fun removeTask(id: Int)= viewModelScope.launch(Dispatchers.IO) {
        taskRepo.removeTask(id)

        val mutableList = mutableListOf<Task>()
        todayTasks.value?.let {
            for(i in todayTasks.value!!){
                if(i.id!=id)
                    mutableList.add(i)
            }
        }

        todayTasks.postValue(mutableList.toList())
    }

    fun onSwitchDay(day: Date)= viewModelScope.launch(Dispatchers.IO) {
        todayTasks.postValue(taskRepo.getTaskByDate(day))
    }

    fun getTypeById(id: Int): Type {
        return runBlocking {
            typeRepo.getTypeById(id)
        }
    }


}
