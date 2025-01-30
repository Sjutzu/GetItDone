package com.example.getitdone.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.getitdone.view.AddTaskScreen
import com.example.getitdone.view.EditTaskScreen
import com.example.getitdone.view.TaskInfoScreen
import com.example.getitdone.view.TaskListScreen
import com.example.getitdone.viewmodel.ToDoViewModel

@Composable
fun ToDoApp() {
    val viewModel: ToDoViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.AddTask.route) {
            AddTaskScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.EditTask.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            EditTaskScreen(navController = navController, viewModel = viewModel, taskId = taskId)
        }

        composable(Screen.TaskInfo.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            TaskInfoScreen(navController = navController, viewModel = viewModel, taskId = taskId)
        }
    }
}
