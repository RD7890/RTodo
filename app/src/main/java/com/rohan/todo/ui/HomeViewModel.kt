package com.rohan.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rohan.todo.data.Task
import com.rohan.todo.data.TaskDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: TaskDao) : ViewModel() {
    val tasks: StateFlow<List<Task>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch { dao.insert(Task(title = title.trim(), description = description.trim())) }
    }
    fun toggleTask(task: Task) = viewModelScope.launch { dao.update(task.copy(isCompleted = !task.isCompleted)) }
    fun updateTask(task: Task) = viewModelScope.launch { dao.update(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { dao.delete(task) }
    fun getTask(id: Long, onResult: (Task?) -> Unit) =
        viewModelScope.launch { onResult(dao.getById(id)) }

    class Factory(private val dao: TaskDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(dao) as T
    }
}
