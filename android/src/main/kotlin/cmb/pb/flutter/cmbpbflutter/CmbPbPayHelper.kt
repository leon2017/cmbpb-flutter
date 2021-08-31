package cmb.pb.flutter.cmbpbflutter

import android.app.Activity
import cmbapi.CMBApi
import cmbapi.CMBApiFactory

/**
 * 一网通支付帮助类
 */
object CmbPbPayHelper {

    private const val APP_ID = ""

    var cmbApi: CMBApi?= null

    fun initialize(activity: Activity){
        cmbApi = CMBApiFactory.createCMBAPI(activity,APP_ID)
    }
}