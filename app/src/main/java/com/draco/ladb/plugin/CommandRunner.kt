package com.draco.ladb.plugin

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess

class CommandRunner : TaskerPluginRunnerAction<CommandInput, CommandOutput>() {

    override fun run(context: Context, input: TaskerInput<CommandInput>): TaskerPluginResult<CommandOutput> {
        val comando = input.regular.command.trim()

        if (comando.isEmpty()) {
            return TaskerPluginResultSucess(
                CommandOutput(output = "ERROR: el comando esta vacio", success = false)
            )
        }

        val salida = try {
            AdbShellManager.sendCommand(context, comando, input.regular.timeoutMs)
        } catch (e: Exception) {
            "ERROR: ${e.message ?: "error desconocido ejecutando el comando"}"
        }

        val huboError = salida.startsWith("ERROR:")

        return TaskerPluginResultSucess(CommandOutput(output = salida, success = !huboError))
    }
}
