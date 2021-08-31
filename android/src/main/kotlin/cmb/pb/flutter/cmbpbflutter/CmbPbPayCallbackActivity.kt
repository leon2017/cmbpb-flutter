package cmb.pb.flutter.cmbpbflutter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CmbPbPayCallbackActivity : AppCompatActivity(){

    companion object {
        const val KEY_CMBPB_CALLBACK = "cmbpb_callback"
        const val KEY_CMBPB_RESP = "cmbpb_resp"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleIntent(data)
    }

    private fun handleIntent(intent: Intent?) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.apply {
            putExtra(KEY_CMBPB_CALLBACK,true)
            putExtra(KEY_CMBPB_RESP, intent)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(launchIntent)
        }
        finish()
    }
}

fun Intent.extraCallback(): Intent? {
    if (extras!=null && getBooleanExtra(CmbPbPayCallbackActivity.KEY_CMBPB_CALLBACK,false)) {
        return getParcelableExtra(CmbPbPayCallbackActivity.KEY_CMBPB_RESP)
    }
    return null
}