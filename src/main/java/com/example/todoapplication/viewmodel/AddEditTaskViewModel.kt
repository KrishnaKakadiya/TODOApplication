package com.example.todoapplication.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.Task
import com.example.todoapplication.data.TaskDao
import com.example.todoapplication.view.ADD_TASK_RESULT_OK
import com.example.todoapplication.view.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskTitle = state.get<String>("taskTitle") ?: task?.title ?: ""
        set(value) {
            field = value
            state.set("taskTitle", value)
        }

    var taskDescription = state.get<String>("taskDescription") ?: task?.description ?: ""
        set(value) {
            field = value
            state.set("taskDescription", value)
        }

    var taskDate = state.get<String>("taskDate") ?: task?.date ?: ""
        set(value) {
            field = value
            state.set("taskDate", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if(taskTitle.isBlank()){
            showInvalidInputMessage("Title cannot be empty")
            return
        }
        if(taskDescription.isBlank()){
            showInvalidInputMessage("Description cannot be empty")
            return
        }
        if(taskDate.isBlank()){
            showInvalidInputMessage("Date cannot be empty")
            return
        }
        if(task !=null){
            val updatedTask = task.copy(title = taskTitle, description = taskDescription, date = taskDate)
            updateTask(updatedTask)
        } else{
            val newTask = Task(title = taskTitle, description = taskDescription, date = taskDate)
            createTask(newTask)
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(s: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(s))
    }

    sealed class AddEditTaskEvent{
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}