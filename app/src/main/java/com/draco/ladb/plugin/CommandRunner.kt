package com.draco.ladb.plugin

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultError
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess

class CommandRunner : TaskerPluginRunnerAction<CommandInput, CommandOutput>() {

    override fun run(context: Context, input: TaskerInput<CommandInput>): TaskerPluginResult<CommandOutput> {
        val comando = input.regular.command.trim()

        if (comando.isEmpty()) {
            return TaskerPluginResultError<CommandOutput>(1, "El comando esta vacio")
        }

        return try {
            val salida = AdbShellManager.sendCommand(
                context,
                comando,
                input.regular.timeoutMs
            )
            val huboError = salida.startsWith("ERROR:")
            TaskerPluginResultSucess(CommandOutput(output = salida, success = !huboError))
        } catch (e: Exception) {
            TaskerPluginResultError<CommandOutput>(2, e.message ?: "Error desconocido ejecutando el comando")
        }
    }
}
