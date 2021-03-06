package com.rhyzue.motion.ui.schedule.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rhyzue.motion.data.AppDatabase
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.TaskDao
import com.rhyzue.motion.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DayViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    public val allTasks: LiveData<List<Task>>

    init {
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val pr = repository.insert(task)
    }

}
