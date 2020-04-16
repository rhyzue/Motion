package com.rhyzue.motion.ui.schedule.day

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhyzue.motion.data.AppDatabase
import com.rhyzue.motion.data.Task
import com.rhyzue.motion.data.TaskDao
import com.rhyzue.motion.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DayViewModel : ViewModel() {
    private val repository: TaskRepository

    init {
        val taskDao = AppDatabase.getDatabase(App.context).TaskDao()
        repository = TaskRepository(taskDao)
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }
}
