package cmb.pb.flutter.cmbpbflutter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cmbapi.CMBRequest
import cmbapi.CMBTitleBar
import cmbapi.CMBWebViewListener
import cmbapi.CMBWebview
import cmbapi.CMBApiUtils

import android.content.Intent
import android.util.Log
import android.widget.Toast

import cmbapi.CMBConstants




class PbWebViewActivity : AppCompatActivity() {

    companion object {
        const val PAYWEB_REQUEST_CODE = 0x110
        const val PAYWEB_URL_PARAMS = "payweb_url_params"
        const val PAYWEB_METHOD_PARAMS = "payweb_method_params"
        const val PAYWEB_DATA_PARAMS = "payweb_data_params"
    }

    private val mStrUrl: String by lazy {
        intent.extras?.getString(PAYWEB_URL_PARAMS, "") ?: ""
    }
    private val mStrMethod: String by lazy {
        intent.extras?.getString(PAYWEB_METHOD_PARAMS, "") ?: ""
    }
    private val mStrRequestData: String by lazy {
        intent.extras?.getString(PAYWEB_DATA_PARAMS, "") ?: ""
    }

    private var mRespCode = 8
    private var mRespString = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pb_webview)
        initEvent()
    }

    private fun initEvent() {
        val webView: CMBWebview = findViewById(R.id.webview)
        val titleBar: CMBTitleBar = findViewById(R.id.titleBar)
        val request = CMBRequest().apply {
            CMBH5Url = mStrUrl
            method = mStrMethod
            requestData = mStrRequestData
        }
        webView.sendRequest(request,object : CMBWebViewListener{
            override fun onClosed(respCode: Int, respString: String?) {
                mRespCode = respCode
                mRespString = respString?:""
                Toast.makeText(this@PbWebViewActivity, "code$respCode === msg$respString", Toast.LENGTH_LONG);
                handleRespMessage()
            }

            override fun onTitleChanged(title: String?) {
                titleBar.setTitle(title?:"")
            }
        })
        titleBar.setOnBackListener {
            webView.cmbResponse.apply {
                mRespCode = respCode;
                mRespString = respMsg;
                handleRespMessage();
            }
        }
    }

    private fun handleRespMessage() {
        Log.d(
            CMBConstants.TAG, "handleRespMessage respCode:" + mRespCode +
                    "respMessage:" + mRespString
        )
        val intent = Intent()
        intent.putExtra(CMBApiUtils.CMBAPI_INTENT_MSG, mRespString)
        intent.putExtra(CMBApiUtils.CMBAPI_INTENT_CODE, mRespCode)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}