package com.rhyzue.motion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rhyzue.motion.data.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepo: TaskRepository
    private val typeRepo: TypeRepository
    private val typesList: LiveData<List<Type>>

    init {
        val taskDao = AppDatabase.getDatabase(application, viewModelScope).taskDao()
        val typeDao = AppDatabase.getDatabase(application, viewModelScope).typeDao()
        taskRepo = TaskRepository(taskDao)
        typeRepo = TypeRepository(typeDao)

        typesList = typeRepo.allTypes
    }

    fun getAllTypes(): LiveData<List<Type>>{
        return typesList
    }

}