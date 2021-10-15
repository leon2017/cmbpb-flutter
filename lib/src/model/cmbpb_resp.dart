import 'package:cmbpbflutter/cmbpbflutter.dart';

///支付结果回调
class CmbPbPayResp {
  const CmbPbPayResp({
    required this.code,
    this.msg,
  });

  /// 错误码
  final int code;

  /// 错误提示字符串
  final String? msg;

  bool get isSuccessful => code == 0;

  factory CmbPbPayResp.fromJson(Map<String, dynamic> json) => CmbPbPayResp(
      code: json[CmbPbConstant.ARGUMENT_PAY_RESPONSE_CODE],
      msg: json[CmbPbConstant.ARGUMENT_PAY_RESPONSE_MSG]);

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data[CmbPbConstant.ARGUMENT_PAY_RESPONSE_CODE] = this.code;
    data[CmbPbConstant.ARGUMENT_PAY_RESPONSE_MSG] = this.msg;
    return data;
  }
}
