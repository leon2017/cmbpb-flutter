//
//  CMBApiManager.h
//  SDKSample  CMBApiDelegate OC 实现
//  Demo 参考，实际没有调用， 已经用SwiftCmbpbflutterPlugin 实现
//

#import <Foundation/Foundation.h>
#import <CMBSDK/CMBApi.h>

@interface CMBApiManager : NSObject <CMBApiDelegate>

+ (instancetype)sharedManager;

@end
