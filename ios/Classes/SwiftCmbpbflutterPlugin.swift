import Flutter
import UIKit

public class SwiftCmbpbflutterPlugin: NSObject, FlutterPlugin {
    
    var appId: String?
    
    var _channel: FlutterMethodChannel?
    
    init(channel: FlutterMethodChannel) {
        _channel = channel
    }
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "cmbpbflutter", binaryMessenger: registrar.messenger())
        let instance = SwiftCmbpbflutterPlugin(channel: channel)
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
        
        let reqObj = CMBRequest.init()
        reqObj.cmbJumpUrl = jumpAppUrl
        reqObj.h5Url = h5Url
        reqObj.method = method
        reqObj.requestData = requestData
        reqObj.navigationBarHidden = isShowBar != nil
        
        let tempAppId = appId ?? ""
        let rootViewController:UIViewController! = UIApplication.shared.keyWindow?.rootViewController
        let cmbDelegate = CMBApiDelegateImpl.shared
        cmbDelegate.respCallback { (code, msg) in
            NSLog("requestPay支付结果==> code: %d, msg: %s",code,msg)
            let map: [String: Any] =
                [CmpPbConstant.ARGUMENT_PAY_RESPONSE_CODE: code, CmpPbConstant.ARGUMENT_PAY_RESPONSE_MSG: msg]
            self._channel?.invokeMethod(CmpPbConstant.METHOD_PAY_RESPONSE, arguments: map)
        }
        CMBApi.send(reqObj, appid: tempAppId, viewController: rootViewController, delegate: cmbDelegate )
    }
}
