package com.draco.ladb.plugin

import android.content.Context
import com.draco.ladb.utils.ADB
import java.io.RandomAccessFile

object AdbShellManager {

    fun sendCommand(context: Context, command: String, timeoutMs: Long = 15_000L): String {
        val adb = ADB.getInstance(context.applicationContext)

        if (adb.running.value != true) {
            val ok = adb.initServer()
            if (!ok) return "ERROR: no se pudo inicializar el servidor adb (¿esta emparejado Wireless Debugging?)"
            Thread.sleep(500)
        }

        val outputFile = adb.outputBufferFile
        val marker = "__TASKER_ADB_DONE_${System.nanoTime()}__"

        val startOffset = if (outputFile.exists()) outputFile.length() else 0L

        adb.sendToShellProcess("$command; echo $marker")

        val startTime = System.currentTimeMillis()
        val collected = StringBuilder()

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (!outputFile.exists()) {
                Thread.sleep(50)
                continue
            }

            RandomAccessFile(outputFile, "r").use { raf ->
                val currentLength = raf.length()
                if (currentLength < startOffset) {
                    raf.seek(0)
                    val buffer = ByteArray(currentLength.toInt())
                    raf.readFully(buffer)
                    collected.setLength(0)
                    collected.append(String(buffer, Charsets.UTF_8))
                } else if (currentLength > startOffset) {
                    raf.seek(startOffset)
                    val bytesToRead = (currentLength - startOffset).toInt()
                    val buffer = ByteArray(bytesToRead)
                    raf.readFully(buffer)
                    val chunk = String(buffer, Charsets.UTF_8)
                    collected.append(chunk)
                }
            }

            if (collected.contains(marker)) {
                val soloResultado = collected.toString().substringBefore(marker)
                return soloResultado.trim()
            }

            Thread.sleep(100)
        }

        return if (collected.isNotEmpty()) {
            collected.toString().trim() + "\n[ERROR: timeout, posiblemente el comando sigue corriendo]"
        } else {
            "ERROR: timeout esperando salida (sin datos en outputBufferFile)"
        }
    }

    fun isAlive(context: Context): Boolean =
        ADB.getInstance(context.applicationContext).running.value == true
}
