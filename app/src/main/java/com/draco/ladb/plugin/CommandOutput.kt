package com.draco.ladb.plugin

import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerOutputObject()
class CommandOutput(
    @get:TaskerOutputVariable("adb_output")
    val output: String,

    @get:TaskerOutputVariable("adb_success")
    val success: Boolean
)
