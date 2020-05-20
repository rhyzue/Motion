package com.rhyzue.motion.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.rhyzue.motion.data.AppDatabase
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application){

    private val taskRepo: TaskRepository

    val todayTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    init{
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        taskRepo = TaskRepository(taskDao)
    }

    fun setTasks(date: Date) = viewModelScope.launch(Dispatchers.IO) {
        todayTasks.postValue(taskRepo.getTaskByDate(date))
    }

}