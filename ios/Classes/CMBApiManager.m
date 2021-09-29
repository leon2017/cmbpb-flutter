//
//  CMBApiManager.m
//  SDKSample  CMBApiDelegate OC 实现
//  Demo 参考，实际没有调用， 已经用SwiftCmbpbflutterPlugin 实现
//

#import "CMBApiManager.h"

@interface CMBApiManager ()
@end

@implementation CMBApiManager

#pragma mark - LifeCycle
+(instancetype)sharedManager {
    static dispatch_once_t onceToken;
    static CMBApiManager *instance;
    dispatch_once(&onceToken, ^{
        instance = [[CMBApiManager alloc] init];
    });
    return instance;
}

#pragma mark - CMBApiDelegate
- (void)onResp:(CMBResponse *)resp {
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *resultStr;
        NSString *detailStr = [NSString stringWithFormat:@"%@(%d)",resp.respMessage,resp.respCode];
        resultStr = @"交易结果";
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:resultStr message:detailStr delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    });
}

@end
