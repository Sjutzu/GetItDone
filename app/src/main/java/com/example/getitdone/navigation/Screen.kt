package com.example.getitdone.navigation

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
    object TaskInfo : Screen("task_info/{taskId}") {
        fun createRoute(taskId: Int) = "task_info/$taskId"
    }
}