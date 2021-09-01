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
    final requestData = "jsonRequestData%3D%7B%22charset%22%3A%22UTF-8%22%2C%22sign%22%3A%227C52658402B98C6D5525F866DA15851BD76F7070730AC893852042B9F8F77562%22%2C%22reqData%22%3A%7B%22dateTime%22%3A%2220190531165802%22%2C%22merchantSerialNo%22%3A%2220190531165802%22%2C%22agrNo%22%3A%222019053116580288%22%2C%22branchNo%22%3A%220755%22%2C%22merchantNo%22%3A%22000054%22%2C%22mobile%22%3A%2213888888888%22%2C%22userID%22%3A%221%22%2C%22lon%22%3A%2250.949506%22%2C%22lat%22%3A%2230.949505%22%2C%22noticeUrl%22%3A%221%22%2C%22noticePara%22%3A%22%22%2C%22returnUrl%22%3A%22simplesdk%3A%2F%2F%22%7D%7D";

    final tempAppUrl = "cmbmobilebank://CMBLS/FunctionJump?action=gofuncid&funcid=200013&serverid=CMBEUserPay&requesttype=post&cmb_app_trans_parms_start=here ";
    final h5Url = "http://99.12.69.116/netpayment/BaseHttp.dll?H5PayJsonSDK";
    CmbPbPay.instance.pay(requestData: requestData, appUrl: tempAppUrl, h5Url: h5Url, method: "pay");
  }
}