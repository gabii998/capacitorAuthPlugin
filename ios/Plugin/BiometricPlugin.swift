import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(BiometricPlugin)
public class BiometricPlugin: CAPPlugin {
    private let implementation = Biometric()

    @objc func echo(_ call: CAPPluginCall) {
        BiometricUtil().login(call)
    }
    
    @objc func deviceSupportBiometricLogin(_ call: CAPPluginCall){
        BiometricUtil().deviceSupportBiometricAuthentication(call)
    }
    
    
}
