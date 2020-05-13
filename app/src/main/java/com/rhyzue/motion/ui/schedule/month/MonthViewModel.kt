package com.rhyzue.motion.ui.schedule.month

import android.app.Application
import androidx.lifecycle.*
import com.rhyzue.motion.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MonthViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepo: TaskRepository
    private val typeRepo: TypeRepository

    val todayTasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    init {
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        val typeDao = AppDatabase.getDatabase(application, viewModelScope).typeDao()
        taskRepo = TaskRepository(taskDao)
        typeRepo = TypeRepository(typeDao)
    }

    fun getTaskById(id: Int): Task {
        return runBlocking{
            taskRepo.getTaskById(id)
        }
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
