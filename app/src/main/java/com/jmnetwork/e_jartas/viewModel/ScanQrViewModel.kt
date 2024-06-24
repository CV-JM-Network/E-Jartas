package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.LoginWebappResponse
import com.jmnetwork.e_jartas.repository.RetrofitClient
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.CustomHandler
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanQrViewModel(application: Application): ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    private fun vibrate(ctx: Context) {
        val vibrator = ContextCompat.getSystemService(ctx, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(200)
        }
    }

    fun validateQRCode(result: String, callback: (String) -> Unit) {
        myPreferences = MySharedPreferences(appContext)

        val idAdmin = myPreferences.getValueInteger(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        if (result.contains("webapp")) {
            vibrate(appContext)
            loginWebApp(appContext, idAdmin, result, tokenAuth) {
                callback(it)
            }
        } else {
            vibrate(appContext)
            callback("qr_code_invalid")
        }
    }

    private fun loginWebApp(ctx: Context, idAdmin: String, deviceId: String, tokenAuth: String, callback: (String) -> Unit) {
        RetrofitClient.apiService.loginWebapp(idAdmin, deviceId, tokenAuth).enqueue(object : Callback<LoginWebappResponse> {
            override fun onResponse(call: Call<LoginWebappResponse>, response: Response<LoginWebappResponse>) {
                when (response.code()) {
                    200 -> {
                        Toasty.success(ctx, "Berhasil login ke webapp", Toasty.LENGTH_LONG).show()
                        callback("webapp_success")
                    }

                    400 -> {
                        val msg = CustomHandler().parseError(response.errorBody()!!.string())
                        CustomHandler().responseHandler(ctx, "loginWebApp|onResponse", msg, 400)
                        callback("webapp_bad_request")
                    }

                    500 -> {
                        CustomHandler().responseHandler(ctx, "loginWebApp|onResponse", "", 500)
                        callback("webapp_internal_server_error")
                    }

                    else -> {
                        val msg = CustomHandler().parseError(response.errorBody()!!.string())
                        callback("webapp_failure")
                        CustomHandler().responseHandler(ctx, "loginWebApp|onResponse", msg)
                    }
                }
            }

            override fun onFailure(call: Call<LoginWebappResponse>, t: Throwable) {
                CustomHandler().responseHandler(ctx, "loginWebApp|onFailure", t.message.toString())
                callback("webapp_failure")
            }
        })

    }
}