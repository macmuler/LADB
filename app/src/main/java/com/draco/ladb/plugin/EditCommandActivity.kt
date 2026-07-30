package com.draco.ladb.plugin

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import androidx.appcompat.app.AppCompatActivity

class EditCommandActivity : AppCompatActivity(), TaskerPluginConfig<CommandInput> {

    override val context get() = applicationContext
    override val inputForTasker get() = TaskerInput(CommandInput(
        command = editComando.text.toString(),
        timeoutMs = editTimeout.text.toString().toLongOrNull() ?: 15000L
    ))

    private lateinit var editComando: EditText
    private lateinit var editTimeout: EditText
    private lateinit var helper: EditCommandActivityHelper

    override fun assignFromInput(input: TaskerInput<CommandInput>) {
        editComando.setText(input.regular.command)
        editTimeout.setText(input.regular.timeoutMs.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = EditCommandActivityHelper(this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        editComando = EditText(this).apply {
            hint = "Comando de shell (ej: settings put system screen_brightness 200)"
        }
        editTimeout = EditText(this).apply {
            hint = "Timeout en ms (default 15000)"
        }

        layout.addView(editComando)
        layout.addView(editTimeout)
        setContentView(layout)

        helper.onCreate()
    }

    override fun onPostCreateWithPreviousResult(input: TaskerInput<CommandInput>, previousBlurb: String) {
        assignFromInput(input)
    }

    override fun onBackPressed() {
        helper.finishForTasker()
    }
}

class EditCommandActivityHelper(config: TaskerPluginConfig<CommandInput>) :
    TaskerPluginConfigHelper<CommandInput, CommandOutput, CommandRunner>(config) {
    override val runnerClass = CommandRunner::class.java
    override val inputClass = CommandInput::class.java
    override val outputClass = CommandOutput::class.java
}
