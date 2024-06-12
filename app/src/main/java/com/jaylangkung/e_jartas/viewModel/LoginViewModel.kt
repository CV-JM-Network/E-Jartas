package com.jaylangkung.e_jartas.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.e_jartas.BuildConfig
import com.jaylangkung.e_jartas.MainActivity
import com.jaylangkung.e_jartas.model.LoginRequest
import com.jaylangkung.e_jartas.model.LoginResponse
import com.jaylangkung.e_jartas.retrofit.RetrofitClient
import com.jaylangkung.e_jartas.utils.Constants
import com.jaylangkung.e_jartas.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(application: Application) : ViewModel() {
    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences
    private var loginRequest: LoginRequest = LoginRequest("", "")

    private val _startActivityEvent = MutableLiveData<String>()
    val startActivityEvent: LiveData<String> get() = _startActivityEvent

    fun setLoginRequest(email: String, password: String) {
        loginRequest = LoginRequest(email, password)
    }

    fun validate(): String {
        fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        return when {
            loginRequest.email.isEmpty() -> "Email tidak boleh kosong"
            !loginRequest.email.isValidEmail() -> "Format email tidak valid"
            loginRequest.password.isEmpty() -> "Kata sandi tidak boleh kosong"
            else -> ""
        }
    }

    @SuppressLint("HardwareIds")
    fun login() {
        myPreferences = MySharedPreferences(appContext)
        val apiKey = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TokenAuth, apiKey)
        val deviceID = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)

        RetrofitClient.apiService.login(
            loginRequest.email,
            loginRequest.password,
            deviceID,
            apiKey
        ).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when (response.code()) {
                    200 -> {
                        val data = response.body()!!.data
                        myPreferences.setValue(Constants.USER, Constants.LOGIN)
                        myPreferences.setValue(Constants.USER_IDADMIN, data.idadmin.toString())
                        myPreferences.setValue(Constants.USER_EMAIL, data.email)
                        myPreferences.setValue(Constants.USER_NAMA, data.nama)
                        myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
                        myPreferences.setValue(Constants.USER_TELP, data.telp)
                        myPreferences.setValue(Constants.USER_FOTO, data.img)
                        myPreferences.setValue(Constants.USER_IDLEVEL, data.idlevel.toString())
                        _startActivityEvent.value = Constants.LOGIN
                    }

                    401 -> {
                        _startActivityEvent.value = "Email atau kata sandi salah"
                    }

                    500 -> {
                        _startActivityEvent.value = "Internal server error"
                    }

                    else -> {
//                        ErrorHandler.parseError(response.errorBody())
                        _startActivityEvent.value = response.errorBody().toString()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                ErrorHandler.parseError(t)
                Toasty.error(appContext, "Login gagal", Toasty.LENGTH_SHORT).show()
                _startActivityEvent.value = "failure"
            }
        })
    }
}