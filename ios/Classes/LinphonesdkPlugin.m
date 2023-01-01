#import "LinphonesdkPlugin.h"
#if __has_include(<linphonesdk/linphonesdk-Swift.h>)
#import <linphonesdk/linphonesdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "linphonesdk-Swift.h"
#endif

@implementation LinphonesdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftLinphonesdkPlugin registerWithRegistrar:registrar];
}
@end
