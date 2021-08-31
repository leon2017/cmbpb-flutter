import 'dart:async';

import 'package:flutter/services.dart';
import 'package:cmbpbflutter/cmnpb_pay_core.dart';

class CmbPbPay {
  CmbPbPay._();

  static CmbPbPay get instance => _instance;

  static final CmbPbPay _instance = CmbPbPay._();

  late final MethodChannel _channel = const MethodChannel('cmbpbflutter')
    ..setMethodCallHandler(_handleMethod);

  final StreamController<CmbPbPayResp> _respStreamController =
      StreamController<CmbPbPayResp>.broadcast();

  ///初始化注册APP ID
  Future<void> registerApp({required String appId}) {
    return _channel.invokeMethod(CmbPbConstant.METHOD_REGISTER_APP,
        <String, dynamic>{CmbPbConstant.ARGUMENT_KEY_APP_ID: appId});
  }

  ///结果回调
  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case CmbPbConstant.METHOD_PAY_RESPONSE:
        _respStreamController.add(CmbPbPayResp.fromJson(
            (call.arguments as Map<dynamic, dynamic>).cast<String, dynamic>()));
        break;
    }
  }
}
