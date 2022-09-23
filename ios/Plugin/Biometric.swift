import Foundation

@objc public class Biometric: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
