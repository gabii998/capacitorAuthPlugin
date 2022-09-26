package ar.com.surtech.biometrics

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Biometric {
    /**
    *   Para chequear si el dispositivo tiene un metodo de bloqueo seteado
     */
    fun checkIfDeviceIsSecure(context: Context):Boolean{
        val keyguardManager:KeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (isMarshMallowOrUpper()){
            keyguardManager.isDeviceSecure
        }else{
            keyguardManager.isKeyguardLocked
        }
    }

    /**
     *   Para chequear si el dispositivo soporta la autenticacion por metodos biometricos
     */
    fun checkIfDeviceSupportsBiometricAuth(context: Context):Boolean{
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }
        suspend fun showBiometricPrompt(activity: AppCompatActivity,title:String? = null,subtitle:String? = null,negativeButtonText:String? = null) = suspendCoroutine{
        val biometricPrompt = BiometricPrompt(activity,object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                it.resume(BiometricAuthError(errString.toString()))
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                it.resume(BiometricAuthSuccess())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                it.resume(BiometricAuthFailed())
            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle(title ?: "Biometric authentication")
            .setSubtitle(subtitle ?: "Authenticate using biometric sensors")
            .setNegativeButtonText(negativeButtonText ?: "Cancel").build()

        biometricPrompt.authenticate(promptInfo)
    }

    suspend fun showBiometricPrompt(fragment:Fragment,title:String?,subtitle:String?,negativeButtonText:String?) = suspendCoroutine{
        val biometricPrompt = BiometricPrompt(fragment,object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                it.resume(BiometricAuthError(errString.toString()))
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                it.resume(BiometricAuthSuccess())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                it.resume(BiometricAuthFailed())
            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle(title ?: "Biometric authentication")
            .setSubtitle(subtitle ?: "Authenticate using biometric sensors")
            .setNegativeButtonText(negativeButtonText ?: "Cancel").build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun isMarshMallowOrUpper():Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    enum class BiometricSupport{
        OK,NO_ENABLED,UNSUPPORTED
    }
    open class BiometricAuthResult
    class BiometricAuthFailed:BiometricAuthResult()
    class BiometricAuthError(errorMessage:String):BiometricAuthResult()
    class BiometricAuthSuccess:BiometricAuthResult()
}