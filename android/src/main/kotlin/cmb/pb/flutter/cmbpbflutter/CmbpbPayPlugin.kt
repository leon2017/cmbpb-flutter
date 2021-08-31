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
import android.util.Log
import android.widget.Toast
import cmbapi.*
import io.flutter.BuildConfig
import io.flutter.plugin.common.PluginRegistry


/** CmbpbflutterPlugin */
class CmbpbPayPlugin : FlutterPlugin, ActivityAware, PluginRegistry.NewIntentListener,
    MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null
    private var applicationContext: Context? = null
    private var activity: Activity? = null
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
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == CmpPbConstant.METHOD_REGISTER_APP) {
            registerApp(call, result)
        } else {
            result.notImplemented()
        }
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
            cmbApi?.handleIntent(resp, eventHandler)
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


}
