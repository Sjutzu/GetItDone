package com.example.getitdone.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.getitdone.navigation.Screen
import com.example.getitdone.viewmodel.ToDoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.ui.text.font.FontWeight


@Composable
fun TaskListScreen(navController: NavController, viewModel: ToDoViewModel) {
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
    var showFilterOptions by remember { mutableStateOf(false) }
    var showSortOptions by remember { mutableStateOf(false) }
    val selectedFilters = remember { mutableStateListOf<String>() }
    var selectedSortOption by remember { mutableStateOf("Tytuł") }

    val filteredTasks = viewModel.filterTasks(tasks, selectedFilters)
    val sortedTasks = viewModel.sortTasks(filteredTasks, selectedSortOption)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "GET IT DONE",
                fontSize = 42.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 62.dp, start = 16.dp, end = 26.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { showFilterOptions = true },
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 48.dp)
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtruj")
            }

            IconButton(
                onClick = { showSortOptions = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Sort, contentDescription = "Sortuj")
            }
        }

        if (showFilterOptions) {
            AlertDialog(
                onDismissRequest = { showFilterOptions = false },
                title = { Text("Filtruj według") },
                text = {
                    Column {
                        listOf(
                            "Priorytet 1", "Priorytet 2", "Priorytet 3",
                            "Mniej niż 3 dni", "Mniej niż tydzień", "Mniej niż miesiąc", "Zaległe"
                        ).forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (selectedFilters.contains(option)) {
                                            selectedFilters.remove(option)
                                        } else {
                                            selectedFilters.add(option)
                                        }
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedFilters.contains(option),
                                    onCheckedChange = {
                                        if (it) {
                                            selectedFilters.add(option)
                                        } else {
                                            selectedFilters.remove(option)
                                        }
                                    }
                                )
                                Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showFilterOptions = false }) {
                        Text("Akceptuj")
                    }
                }
            )
        }

        if (showSortOptions) {
            AlertDialog(
                onDismissRequest = { showSortOptions = false },
                title = { Text("Sortuj według") },
                text = {
                    Column {
                        listOf("Tytuł", "Data", "Priorytet").forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSortOption = option
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedSortOption == option,
                                    onClick = { selectedSortOption = option }
                                )
                                Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showSortOptions = false }) {
                        Text("Akceptuj")
                    }
                }
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sortedTasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray)
                        .clickable { navController.navigate(Screen.TaskInfo.createRoute(task.id)) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = task.title, fontSize = 18.sp)

                        Text(
                            text = "${"★".repeat(task.priority)}",
                            fontSize = 24.sp,
                            color = Color(0xFFFFD700)
                        )
                    }

                    if (task.date != "undefined") {
                        val daysLeft = viewModel.daysUntil(task.date)
                        val daysText = if (daysLeft < 0) "Zaległe" else "$daysLeft dni"
                        Text(text = "Dni do końca: $daysText", fontSize = 14.sp)
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Screen.AddTask.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
        ) {
            Text(
                text = "NOWE ZADANIE",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
