package com.example.getitdone.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.getitdone.model.Task
import com.example.getitdone.viewmodel.ToDoViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.getitdone.navigation.Screen


@Composable
fun EditTaskScreen(navController: NavController, viewModel: ToDoViewModel, taskId: Int) {
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
    val taskToEdit = tasks.find { it.id == taskId }

    if (taskToEdit != null) {
        var title by remember { mutableStateOf(taskToEdit.title) }
        var description by remember { mutableStateOf(taskToEdit.description) }
        var priority by remember { mutableStateOf(taskToEdit.priority) }
        var selectedDate by remember { mutableStateOf(taskToEdit.date) }

        val context = LocalContext.current
        val goldColor = Color(0xFFFFD700)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 80.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Powrót",
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = "Edytuj Zadanie",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = title,
                    onValueChange = { if (it.length <= 43) title = it },
                    label = { Text("Tytuł") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = description,
                    onValueChange = { if (it.length <= 216) description = it },
                    label = { Text("Opis") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Priorytet:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listOf(1, 2, 3).forEach { starValue ->
                        IconButton(
                            onClick = { priority = starValue },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Text(
                                text = if (priority >= starValue) "★" else "☆",
                                fontSize = 40.sp,
                                color = goldColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        viewModel.showDatePickerDialog(context) { newDate ->
                            selectedDate = newDate
                        }
                    }
                ) {
                    Text(
                        text = "Wybierz datę",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Data:\n $selectedDate",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.addTask(
                            Task(
                                id = taskToEdit.id,
                                title = title,
                                description = description,
                                date = selectedDate,
                                priority = priority
                            )
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                ) {
                    Text(
                        text = "Zaktualizuj",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.deleteTask(taskToEdit) // Usuń zadanie
                        navController.navigate(Screen.TaskList.route) {
                            popUpTo(Screen.TaskList.route) { // Wyczyść stos nawigacji do TaskList
                                inclusive = true // Usuń również TaskList ze stosu (opcjonalnie)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = "Usuń",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}