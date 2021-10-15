# CmbpbFlutter
![pub package](https://img.shields.io/badge/pub-v1.0.1-blue)

Flutter 招商一网通支付 SDK。

## 相关文档
* [招商一网通支付接入文档](http://openhome.cmbchina.com/PayNew/pay/doc/cell/app)
* [Universal Links](https://developer.apple.com/documentation/uikit/inter-process_communication/allowing_apps_and_websites_to_link_to_your_content)

## 添加依赖

在`pubspec.yaml` 文件中添加`cmbpbflutter`依赖:
![pub package](https://img.shields.io/badge/pub-v1.0.1-blue)

```yaml
dependencies:
  cmbpbflutter: ^${latestVersion}
```

## 相关配置

### Android 

#### 需要在Android工程AndroidManifest.xml下配置回调入口, 并配置招行分配给商户的「URL scheme」

```
       <activity
           android:name="cmb.pb.flutter.cmbpbflutter.CmbPbPayCallbackActivity"
           android:label="@string/app_name"
           android:exported="true"
           android:theme="@style/CmbPbPay.Theme.Transparent"
           android:taskAffinity="${applicationId}"
           android:launchMode="singleTop">

           <intent-filter>
               <action android:name="android.intent.action.VIEW"/>
               <category android:name="android.intent.category.DEFAULT"/>
               <data android:scheme="${招行给的回调url scheme}"/>
           </intent-filter>
       </activity>
```

#### 需要在Android工程的根目录下的`build.gradle`文件里的`allprojects`添加cmbpbflutter 依赖的aar libs
```gradle
allprojects {
    repositories {
        ...
        flatDir {
            dirs 'libs', project(':cmbpbflutter').file('libs')
        }
    }
}
```

### IOS 

#### 配置Xcode中
* 在info.plist中增加LSApplicationQueriesSchemes白名单配置，值为cmbmobilebank。
* 在URL Types中新增URL Schemes，值设置为招行分配给商户的scheme（需与安卓设置的scheme保持一致），如分配到的scheme为abcdefg则配置如下图所示。

<image src="https://github.com/leon2017/cmbpb-flutter/blob/master/screenshot/screenshot_1.png"/>

## 基本使用

* [示例代码](https://github.com/leon2017/cmbpb-flutter/blob/master/example/lib/main.dart)

```

// 注册appId
CmbPbPay.instance.registerApp(appId: "12306");

//设置支付回调
CmbPbPay.instance.respStream().listen((event) {
      print("支付结果回调了");
      setState(() {
        _payResultDesc = "${event.code} ===${event.msg}";
      });
    });

//发起支付
void _startPay() {
    final requestData = "xxx";
    final appUrl = "cmbmobilebank://CMBLS/FunctionJump?action=gofuncid&funcid=200013&serverid=CMBEUserPay&requesttype=post&cmb_app_trans_parms_start=here";
    final h5Url = "http://121.15.180.66:801/netpayment/BaseHttp.dll?H5PayJsonSDK";
    CmbPbPay.instance.pay(requestData: requestData, appUrl: appUrl, h5Url: h5Url, method: "pay");
}

```

## 捐助
开源不易，请作者喝杯咖啡。

<img src="https://github.com/leon2017/cmbpb-flutter/blob/master/screenshot/wx.jpg" height="300">  <img src="https://github.com/leon2017/cmbpb-flutter/blob/master/screenshot/alipay.jpg" height="300">

## LICENSE

    Copyright 2021 Leon2017 CmbpbFlutter Project

    Licensed to the Apache Software Foundation (ASF) under one or more contributor
    license agreements.  See the NOTICE file distributed with this work for
    additional information regarding copyright ownership.  The ASF licenses this
    file to you under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License.  You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
    License for the specific language governing permissions and limitations under
    the License.
