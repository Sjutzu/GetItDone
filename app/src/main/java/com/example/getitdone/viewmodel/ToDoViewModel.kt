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
import java.text.SimpleDateFormat
import java.util.Calendar


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

    val selectedDate = mutableStateOf("")
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

    fun daysUntil(dateString: String): Int {
        if (dateString.isBlank() || dateString == "undefined") {
            return Int.MAX_VALUE
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val taskDate = dateFormat.parse(dateString) ?: return Int.MAX_VALUE
            val today = Calendar.getInstance().time
            val diff = taskDate.time - today.time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: java.text.ParseException) {
            Int.MAX_VALUE
        }
    }

    fun filterTasks(tasks: List<Task>, selectedFilters: List<String>): List<Task> {
        return tasks.filter { task ->
            val matchesPriority = selectedFilters.isEmpty() || selectedFilters.any { filter ->
                when (filter) {
                    "Priorytet 1" -> task.priority == 1
                    "Priorytet 2" -> task.priority == 2
                    "Priorytet 3" -> task.priority == 3
                    else -> false
                }
            }
            val matchesDate = selectedFilters.isEmpty() || selectedFilters.any { filter ->
                val daysLeft = daysUntil(task.date)
                when (filter) {
                    "Mniej niż 3 dni" -> daysLeft < 3
                    "Mniej niż tydzień" -> daysLeft < 7
                    "Mniej niż miesiąc" -> daysLeft < 30
                    "Zaległe" -> daysLeft < 0
                    else -> false
                }
            }
            matchesPriority || matchesDate
        }
    }

    fun sortTasks(tasks: List<Task>, selectedSortOption: String): List<Task> {
        return when (selectedSortOption) {
            "Tytuł" -> tasks.sortedBy { it.title }
            "Data" -> tasks.sortedBy { it.date }
            "Priorytet" -> tasks.sortedBy { it.priority }
            else -> tasks
        }
    }
}