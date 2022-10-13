package ar.com.multiplatform.biometrics

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall

object Biometric {
    /**
     *   Para chequear si el dispositivo tiene un metodo de bloqueo seteado
     */

    private var trigger:Boolean = true


    fun checkIfDeviceIsSecure(context: Context): Boolean {
        val keyguardManager: KeyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (isMarshMallowOrUpper()) {
            keyguardManager.isDeviceSecure
        } else {
            keyguardManager.isKeyguardLocked
        }
    }

    /**
     *   Para chequear si el dispositivo soporta la autenticacion por metodos biometricos
     */
    fun checkIfDeviceSupportsBiometricAuth(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun showBiometricPrompt(
        activity: AppCompatActivity,
        title: String? = null,
        subtitle: String? = null,
        negativeButtonText: String? = null,
        call: PluginCall
    ) {
        var biometricPrompt: BiometricPrompt? = null
        biometricPrompt =
            BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (trigger){
                        call.resolve(generateJson("Error"))
                        biometricPrompt?.cancelAuthentication()
                    }

                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    call.resolve(generateJson("Ok"))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    call.resolve(generateJson("Failed"))
                    trigger = false
                    biometricPrompt?.cancelAuthentication()
                }
            })
        val promptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle(title ?: "Biometric authentication")
                .setSubtitle(subtitle ?: "Authenticate using biometric sensors")
                .setNegativeButtonText(negativeButtonText ?: "Cancel").build()

        biometricPrompt.authenticate(promptInfo)
    }

    suspend fun showBiometricPrompt(
        fragment: Fragment,
        title: String?,
        subtitle: String?,
        negativeButtonText: String?,
        call: PluginCall
    ) {
        val biometricPrompt =
            BiometricPrompt(fragment, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    call.resolve(generateJson("ERROR"))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    call.resolve(generateJson("OK"))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    call.resolve(generateJson("FAILED"))
                }
            })
        val promptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle(title ?: "Biometric authentication")
                .setSubtitle(subtitle ?: "Authenticate using biometric sensors")
                .setNegativeButtonText(negativeButtonText ?: "Cancel").build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun generateJson(value: String): JSObject {
        val obj = JSObject()
        obj.put("value", value)
        return obj
    }

    fun generateJsonBoolean(value:Boolean):JSObject{
        val obj = JSObject()
        obj.put("value", value)
        return obj
    }

    private fun isMarshMallowOrUpper(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    enum class BiometricSupport {
        OK, NO_ENABLED, UNSUPPORTED
    }

    open class BiometricAuthResult
    class BiometricAuthFailed : BiometricAuthResult()
    class BiometricAuthError(errorMessage: String) : BiometricAuthResult()
    class BiometricAuthSuccess : BiometricAuthResult()
}