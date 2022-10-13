package ar.com.multiplatform.biometrics;


import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;
import com.getcapacitor.Plugin;

@CapacitorPlugin(name = "Biometric")
public class BiometricPlugin extends Plugin {

    @PluginMethod
    public void echo(final PluginCall call) {
        showBiometric(call);
    }

    @PluginMethod
    public void deviceSupportBiometricLogin(final PluginCall call){
        call.resolve(Biometric.INSTANCE.generateJsonBoolean(Biometric.INSTANCE.checkIfDeviceSupportsBiometricAuth(getContext())));
    }

    public void showBiometric(final PluginCall call) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            if (Biometric.INSTANCE.checkIfDeviceSupportsBiometricAuth(getContext())) {
                Biometric.INSTANCE.showBiometricPrompt(getActivity(),null,null,null, call);
            } else {
                Biometric.INSTANCE.generateJson("Unsupported");
            }
        });
    }

}