package com.draco.ladb.plugin

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class CommandInput @JvmOverloads constructor(
    @field:TaskerInputField("command") var command: String = "",
    @field:TaskerInputField("timeoutMs") var timeoutMs: Long = 15000L
)
