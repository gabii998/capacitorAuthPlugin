package ar.com.surtech.biometrics

import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.PluginMethod
import com.getcapacitor.PluginCall
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CapacitorPlugin(name = "Biometric")
class BiometricPlugin : Plugin() {
    val scope = CoroutineScope(Dispatchers.Main)
    private val implementation = Biometric()

    @PluginMethod
    fun echo(call: PluginCall) {
        scope.launch {
            val ret = JSObject()
            ret.put("value", showBiometric())
            call.resolve(ret)
        }
    }

    suspend fun showBiometric():String {
        return if (implementation.checkIfDeviceSupportsBiometricAuth(context)) {
            launchBiometric()
        } else {
            "UNSUPPORTED"
        }
    }

    private suspend fun launchBiometric(): String {
        return when (implementation.showBiometricPrompt(activity)) {
            is Biometric.BiometricAuthSuccess -> "OK"
            is Biometric.BiometricAuthFailed -> "FAILED"
            else -> "ERROR"
        }
    }
}