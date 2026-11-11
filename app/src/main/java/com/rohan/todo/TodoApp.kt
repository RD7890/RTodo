package com.rohan.todo

import android.app.Application
import com.rohan.todo.data.AppDatabase

class TodoApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}
