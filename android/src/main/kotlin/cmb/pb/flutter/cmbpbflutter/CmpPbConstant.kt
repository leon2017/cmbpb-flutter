package cmb.pb.flutter.cmbpbflutter

object CmpPbConstant {

    const val METHOD_REGISTER_APP = "registerApp"
    const val METHOD_PAY_RESPONSE = "payResponse"
    const val METHOD_PAY_REQUEST = "payRequest"

    const val ARGUMENT_KEY_APP_ID = "appId"
    const val ARGUMENT_PAY_RESPONSE_CODE = "payResponseCode"
    const val ARGUMENT_PAY_RESPONSE_MSG = "payResponseMsg"

    const val ARGUMENT_REQUEST_DATA = "requestData"
    const val ARGUMENT_REQUEST_JUMP_APP_URL = "requestJumpAppUrl"
    const val ARGUMENT_REQUEST_H5_URL = "requestH5Url"
    const val ARGUMENT_REQUEST_METHOD = "requestMethod"
    const val ARGUMENT_REQUEST_SHOW_BAR = "requestShowBar"
}


enum class PayResult(
    val code: Int,
    val message: String,
    val detail: Any ? = null
){

    RESPONSE_SUCCESS(0,"支付成功"),
    REQUEST_JUMP_APP_URL_EMPTY(-1000, "参数错误","调用失败,cmbJumpUrl不能为空"),
    REQUEST_H5_URL_EMPTY(-1001, "参数错误","调用失败,h5Url不能为空"),
    REQUEST_PARAMS_ERROR(-1002,"参数错误")
}