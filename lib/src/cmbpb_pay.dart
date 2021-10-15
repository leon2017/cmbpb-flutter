import 'dart:async';

import 'package:flutter/services.dart';
import 'package:cmbpbflutter/cmbpbflutter.dart';

class CmbPbPay {
  CmbPbPay._();

  static CmbPbPay get instance => _instance;

  static final CmbPbPay _instance = CmbPbPay._();

  late final MethodChannel _channel = const MethodChannel('cmbpbflutter')
    ..setMethodCallHandler(_handleMethod);

  final StreamController<CmbPbPayResp> _respStreamController =
      StreamController<CmbPbPayResp>.broadcast();


  Stream<CmbPbPayResp> respStream() {
    return _respStreamController.stream;
  }

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

  Future<void> pay(
      {required String requestData,
      required String appUrl,
      required String h5Url,
      required String method,
      bool showBar = true}) {
    return _channel.invokeMethod<void>(
      CmbPbConstant.METHOD_PAY_REQUEST,
      <String, dynamic>{
        CmbPbConstant.ARGUMENT_REQUEST_DATA: requestData,
        CmbPbConstant.ARGUMENT_REQUEST_JUMP_APP_URL: appUrl,
        CmbPbConstant.ARGUMENT_REQUEST_H5_URL: h5Url,
        CmbPbConstant.ARGUMENT_REQUEST_METHOD: method,
        CmbPbConstant.ARGUMENT_REQUEST_SHOW_BAR: showBar
      },
    );
  }
}
