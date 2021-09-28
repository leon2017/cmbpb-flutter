import Flutter
import UIKit

public class SwiftCmbpbflutterPlugin: NSObject, FlutterPlugin {
    
    var appId: String?
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "cmbpbflutter", binaryMessenger: registrar.messenger())
        let instance = SwiftCmbpbflutterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let _method =  call.method
        switch _method {
        case CmpPbConstant.METHOD_REGISTER_APP:
            registerApp(call,result: result)
            break
        case CmpPbConstant.METHOD_PAY_REQUEST:
            requestPay(call,result: result)
            break
        default:
            result(nil)
            break
        }
    }
    
    
    private func registerApp(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        NSLog("registerApp被调用")
        let arguments = call.arguments as? [String: Any] ?? [String: Any]()
        appId = arguments[CmpPbConstant.ARGUMENT_KEY_APP_ID] as? String
    }
    
    private func requestPay(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        NSLog("requestPay发起支付")
        let arguments = call.arguments as? [String: Any] ?? [String: Any]()
        let requestData = arguments[CmpPbConstant.ARGUMENT_REQUEST_DATA] as? String
        let jumpAppUrl = arguments[CmpPbConstant.ARGUMENT_REQUEST_JUMP_APP_URL] as? String
        let h5Url = arguments[CmpPbConstant.ARGUMENT_REQUEST_H5_URL] as? String
        let method = arguments[CmpPbConstant.ARGUMENT_REQUEST_METHOD] as? String
        let isShowBar = arguments[CmpPbConstant.ARGUMENT_REQUEST_SHOW_BAR] as? Bool
    
//        let reqObj = [[CMBRequest alloc] init];
    }
}
