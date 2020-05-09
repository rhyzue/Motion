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


    val allTasks: LiveData<List<Task>>
    val todayTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }
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
        taskRepo.insert(task)
    }

    fun getTaskById(id: Int): Task {
        return runBlocking{
            taskRepo.getTaskById(id)
        }
    }

    fun modifyTask(task: Task)= viewModelScope.launch(Dispatchers.IO) {
        taskRepo.modifyTask(task)
    }

    fun removeTask(id: Int)= viewModelScope.launch(Dispatchers.IO) {
        taskRepo.removeTask(id)
    }

    fun onSwitchDay(day: Date)= viewModelScope.launch(Dispatchers.IO) {
        //todayTasks.postValue(taskRepo.getTaskByDate(day))
        val list = taskRepo.getTaskByDate(day)
        for(i in list){
            println(i)
        }
        todayTasks.postValue(list)
    }
/*
    fun setAdapter(adapter: TaskListAdapter){
        todayTasks.observe(viewLifecycleOwner, Observer{ tasks ->
            tasks?.let { adapter.setTasks(it) }
    }*/

}
