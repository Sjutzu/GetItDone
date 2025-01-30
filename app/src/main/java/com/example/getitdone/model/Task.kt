package com.example.getitdone.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "undefined",

    @ColumnInfo(name = "description")
    val description: String = "undefined",

    @ColumnInfo(name = "date")
    val date: String = "undefined",

    @ColumnInfo(name = "priority")
    val priority: Int = 1
)