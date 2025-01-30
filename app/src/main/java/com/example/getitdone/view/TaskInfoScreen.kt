package com.example.getitdone.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.getitdone.viewmodel.ToDoViewModel
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.getitdone.navigation.Screen
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme



@Composable
fun TaskInfoScreen(navController: NavController, viewModel: ToDoViewModel, taskId: Int) {
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
    val taskToView = tasks.find { it.id == taskId }

    if (taskToView != null) {
        val title = taskToView.title
        val description = taskToView.description
        val priority = taskToView.priority
        val selectedDate = taskToView.date

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
                    text = "Szczegóły Zadania",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = description,
                            fontSize = 18.sp,
                        )
                    }
                }

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
                            onClick = {},
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
                        navController.navigate(Screen.EditTask.createRoute(taskId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                ) {
                    Text(
                        text = "Edytuj zadanie",
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                ) {
                    Text(
                        text = "Wróć do listy zadań",
                        fontSize = 20.sp
                    )
                }
            }
        }
    } else {
        Text("Zadanie nie zostało znalezione.", fontSize = 18.sp)
    }
}