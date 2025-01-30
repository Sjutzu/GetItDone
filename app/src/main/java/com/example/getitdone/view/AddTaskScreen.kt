package com.example.getitdone.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.getitdone.model.Task
import com.example.getitdone.viewmodel.ToDoViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AddTaskScreen(navController: NavController, viewModel: ToDoViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedDate = viewModel.selectedDate.value
    val priority = viewModel.priority.value

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
            Text(
                text = "Dodaj Zadanie",
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
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
                modifier = Modifier.fillMaxWidth(),
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
                        onClick = { viewModel.setPriority(starValue) },
                        modifier = Modifier
                            .size(50.dp)
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
                        viewModel.setSelectedDate(newDate)
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
                text = "Data:\n ${viewModel.selectedDate.value}",
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
                    when {
                        title.isEmpty() -> {
                            android.widget.Toast.makeText(context, "Proszę wpisać tytuł zadania!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        description.isEmpty() -> {
                            android.widget.Toast.makeText(context, "Proszę wpisać opis zadania!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        selectedDate.isEmpty() -> {
                            android.widget.Toast.makeText(context, "Proszę wybrać datę i godzinę!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        priority == 0 -> {
                            android.widget.Toast.makeText(context, "Proszę wybrać priorytet zadania!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.addTask(Task(title = title, description = description, date = selectedDate, priority = priority))
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
            ) {
                Text(
                    text = "Dodaj",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Anuluj",
                    fontSize = 20.sp
                )
            }
        }
    }
}