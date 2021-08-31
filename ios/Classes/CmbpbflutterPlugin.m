#import "CmbpbflutterPlugin.h"
#if __has_include(<cmbpbflutter/cmbpbflutter-Swift.h>)
#import <cmbpbflutter/cmbpbflutter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "cmbpbflutter-Swift.h"
#endif

@implementation CmbpbflutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCmbpbflutterPlugin registerWithRegistrar:registrar];
}
@end
