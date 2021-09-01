package cmb.pb.flutter.cmbpbflutter

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import cmbapi.*
import io.flutter.BuildConfig
import io.flutter.plugin.common.PluginRegistry


/** CmbpbflutterPlugin */
class CmbpbPayPlugin : FlutterPlugin, ActivityAware, PluginRegistry.NewIntentListener,
    PluginRegistry.ActivityResultListener,
    MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null
    private var applicationContext: Context? = null
    private var activity: Activity? = null
    private var activityPluginBinding: ActivityPluginBinding? = null
    var cmbApi: CMBApi? = null

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "cmbpbflutter")
        channel?.setMethodCallHandler(this)
        applicationContext = binding.applicationContext
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
        applicationContext = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        activity = null
        activityPluginBinding?.removeActivityResultListener(this)
        activityPluginBinding = null
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            CmpPbConstant.METHOD_REGISTER_APP -> {
                registerApp(call, result)
            }
            CmpPbConstant.METHOD_PAY_REQUEST -> {
                requestPay(call, result)
            }
            else -> {
                result.notImplemented()
            }
        }
    }


    /**
     * start pay
     */
    private fun requestPay(call: MethodCall, result: Result) {
        val requestData = call.argument<String>(CmpPbConstant.ARGUMENT_REQUEST_DATA)
        val jumpAppUrl = call.argument<String>(CmpPbConstant.ARGUMENT_REQUEST_JUMP_APP_URL)
        val h5Url = call.argument<String>(CmpPbConstant.ARGUMENT_REQUEST_H5_URL)
        val method = call.argument<String>(CmpPbConstant.ARGUMENT_REQUEST_METHOD)
        val isShowBar = call.argument<Boolean>(CmpPbConstant.ARGUMENT_REQUEST_SHOW_BAR) ?: true
        val request = createCmbRequest(requestData, jumpAppUrl, h5Url, method, isShowBar)
        if (TextUtils.isEmpty(h5Url)) {
            result.error(
                "${PayResult.REQUEST_H5_URL_EMPTY.code}",
                PayResult.REQUEST_H5_URL_EMPTY.message,
                PayResult.REQUEST_H5_URL_EMPTY.detail
            )
            return
        }
        if (TextUtils.isEmpty(jumpAppUrl)) {
            result.error(
                "${PayResult.REQUEST_JUMP_APP_URL_EMPTY.code}",
                PayResult.REQUEST_JUMP_APP_URL_EMPTY.message,
                PayResult.REQUEST_JUMP_APP_URL_EMPTY.detail
            )
            return
        }
        cmbApi?.sendReq(request)
        result.success(null)
    }

    /**
     * new CMBRequest
     */
    private fun createCmbRequest(
        requestData: String?,
        jumpAppUrl: String?,
        h5Url: String?,
        method: String?,
        isShowBar: Boolean
    ): CMBRequest {
        val request = CMBRequest()
        request.requestData = requestData
        request.CMBJumpAppUrl = jumpAppUrl
        request.CMBH5Url = h5Url
        request.method = method
        request.isShowNavigationBar = isShowBar
        return request
    }

    /**
     * 一网通 init
     */
    private fun registerApp(call: MethodCall, result: Result) {
        val appId = call.argument<String>(CmpPbConstant.ARGUMENT_KEY_APP_ID)
        cmbApi = CMBApiFactory.createCMBAPI(activity, appId)
        Log.d(CMBConstants.TAG, "registerApp AppId:$appId")
        result.success(null)
    }

    override fun onNewIntent(intent: Intent?): Boolean {
        val resp = intent?.extraCallback()
        if (resp != null) {
            handleIntent(resp)
            return true
        }
        return false
    }


    private val eventHandler = CMBEventHandler { response ->
        Log.d(CMBConstants.TAG, "MainActivity-onResp 进入:")
        if (response.respCode == 0) {
            Log.d(
                CMBConstants.TAG,
                "MainActivity-onResp responseMSG:" + response.respMsg + "responseCODE= " + response.respCode
            );
            Toast.makeText(activity, "调用成功.str:" + response.respMsg, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(CMBConstants.TAG, "MainActivity-onResp 调用失败:");
            Toast.makeText(activity, "调用失败", Toast.LENGTH_SHORT).show();
        }
        val resMsg = "onResp：respcode:${response.respCode}.respmsg:${response.respMsg}"
        Log.d(CMBConstants.TAG, resMsg)
        val tempMap = mutableMapOf<String, Any>()
        tempMap[CmpPbConstant.ARGUMENT_PAY_RESPONSE_CODE] = response.respCode
        tempMap[CmpPbConstant.ARGUMENT_PAY_RESPONSE_MSG] = response.respMsg
        channel?.invokeMethod(CmpPbConstant.METHOD_PAY_RESPONSE, tempMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d(CMBConstants.TAG, "CmbPbPayCallbackActivity handleIntent2" +data?.dataString)
        if (requestCode == 3 && resultCode == 2){
            handleIntent(data)
            return true
        }
        return false
    }

    private fun handleIntent(resp: Intent?) {
        cmbApi?.handleIntent(resp, eventHandler)
    }


}
