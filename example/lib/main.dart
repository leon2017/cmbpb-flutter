import 'package:flutter/material.dart';
import 'dart:async';
import 'package:cmbpbflutter/cmnpb_pay_core.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  StreamSubscription<CmbPbPayResp>? _respSubs;

  String _payResultDesc = "";

  @override
  void initState() {
    super.initState();
    _respSubs = CmbPbPay.instance.respStream().listen((event) {
      print("支付结果回调了");
      setState(() {
        _payResultDesc = "${event.code} ===${event.msg}";
      });
    });
    CmbPbPay.instance.registerApp(appId: "12306");
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('招行一网通支付demo'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: _startPay,
                child: Text("支付"),
              ),
              Text("支付结果回调：$_payResultDesc")
            ],
          ),
        ),
      ),
    );
  }

  void _startPay() {
    //Url 编码后的结果
    final requestData = "jsonRequestData=%7B%22charset%22%3A%22UTF-8%22%2C%22reqData%22%3A%7B%22amount%22%3A%2240.00%22%2C%22branchNo%22%3A%220021%22%2C%22date%22%3A%2220210927%22%2C%22dateTime%22%3A%2220210927165358%22%2C%22expireTimeSpan%22%3A%2210%22%2C%22merchantNo%22%3A%22000719%22%2C%22orderNo%22%3A%222109271000098854%22%2C%22payNoticeUrl%22%3A%22https%3A%2F%2Fapp.mjcampus.com%2Fmpay%2Fv100%2Fnotify_cmbchina%22%2C%22returnUrl%22%3A%22aacbaaahbj%3A%2F%2F%22%7D%2C%22sign%22%3A%221566368d8dcf5782add5439b3cdb5f83047f87cc6f35c7903e65916e97df1b0b%22%2C%22signType%22%3A%22SHA-256%22%2C%22version%22%3A%221.0%22%7D";
    final tempAppUrl = "cmbmobilebank://CMBLS/FunctionJump?action=gofuncid&funcid=200013&serverid=CMBEUserPay&requesttype=post&cmb_app_trans_parms_start=here";
    final h5Url = "http://121.15.180.66:801/netpayment/BaseHttp.dll?H5PayJsonSDK";
    CmbPbPay.instance.pay(requestData: requestData, appUrl: tempAppUrl, h5Url: h5Url, method: "pay");
  }
}