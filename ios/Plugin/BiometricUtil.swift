//
//  BiometricUtil.swift
//  Plugin
//
//  Created by Gabriel Ascurra on 23/09/2022.
//


import Foundation
import LocalAuthentication
import Capacitor

class BiometricUtil{
    func login(_ call: CAPPluginCall) {
        let context = LAContext()
        context.localizedFallbackTitle = "Authenticate with your face"
        var authError:NSError?
        
        let reasonString = "To authenticate"
        if context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &authError) {
            context.evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason:reasonString){success,error in
                if success {
                    call.resolve([
                        "value": "Ok"
                    ])
                }else{
                    call.resolve([
                        "value": "Failed"
                    ])
                }
                
            }
        }else{
            call.resolve([
                "value": "Error"
            ])
        }
        
    }
}

