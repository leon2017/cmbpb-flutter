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
import cmbapi.CMBApi
import cmbapi.CMBApiFactory
import io.flutter.plugin.common.PluginRegistry


/** CmbpbflutterPlugin */
class CmbpbPayPlugin : FlutterPlugin, ActivityAware, PluginRegistry.NewIntentListener, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null
    private var applicationContext: Context? = null
    private var activity: Activity? = null
    var cmbApi: CMBApi?= null

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
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
            cmbApi = CMBApiFactory.createCMBAPI(activity, "")
        } else {
            result.notImplemented()
        }
    }

    override fun onNewIntent(intent: Intent?): Boolean {
        val resp = intent?.extraCallback()
        if (resp!=null){
            //TODO 招行支付回调
            return true
        }
        return false
    }

}
