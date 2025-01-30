package com.example.getitdone.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.getitdone.model.Task
import com.example.getitdone.model.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }


    val selectedDate =  mutableStateOf("")
    val priority = mutableStateOf(0)

    fun setSelectedDate(newDate: String) {
        selectedDate.value = newDate
    }
    fun setPriority(newPriority: Int) {
        priority.value = newPriority
    }

    fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }
}