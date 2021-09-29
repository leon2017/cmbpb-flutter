//
//  CMBApiDelegate 代理实现类
//  cmbpbflutter
//
//  Created by leon on 2021/9/29.
//

import Foundation

typealias CmbRespCallback = (_ code:Int, _ msg: String ) -> Void

class CMBApiDelegateImpl: NSObject, CMBApiDelegate {
    
    static var shared: CMBApiDelegateImpl = {
        let instance = CMBApiDelegateImpl()
        return instance
    }()
    
    private override init() {}
    
    private var callback: CmbRespCallback?
    
    func respCallback(block: @escaping CmbRespCallback) {
        self.callback = block
    }
    
    func onResp(_ resp: CMBResponse!) {
//        DispatchQueue.main.async(execute: {
//                    var resultStr: String?
//                    var detailStr: String? = nil
//                    if let respMessage = resp?.respMessage, let respCode = resp?.respCode {
//                        detailStr = "\(respMessage)(\(respCode))"
//                    }
//                    resultStr = "交易结果"
//
//            let alert = UIAlertView(title: resultStr ?? <#default value#>, message: detailStr ?? <#default value#>, delegate: self, cancelButtonTitle: "OK", otherButtonTitles: "")
//                    alert.show()
//                })
        NSLog("支付回调了！")
        DispatchQueue.main.async {
            let respMessage = resp?.respMessage ?? "未知错误"
            let respCode = resp?.respCode ?? -1000
            if (self.callback != nil) {
                self.callback!(Int(respCode), respMessage)
            }
        }
    }
}
