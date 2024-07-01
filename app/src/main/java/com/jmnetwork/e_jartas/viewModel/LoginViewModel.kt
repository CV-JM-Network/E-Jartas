package com.jmnetwork.e_jartas.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.BuildConfig
import com.jmnetwork.e_jartas.model.LoginRequest
import com.jmnetwork.e_jartas.repository.BaseRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class LoginViewModel(application: Application) : ViewModel() {
    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences
    private var loginRequest: LoginRequest = LoginRequest("", "")
    private val repository = BaseRepositoryImpl()

    val startActivityEvent = MutableLiveData<String>()

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
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)

        val deviceID = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
        val loginResponse = repository.login(appContext, loginRequest, deviceID, tokenAuth)

        loginResponse.observeForever { response ->
            if (response.data != null) {
                val data = response.data
                myPreferences.setValue(Constants.USER, Constants.LOGIN)
                myPreferences.setValueInteger(Constants.USER_IDADMIN, data.idAdmin)
                myPreferences.setValue(Constants.USER_EMAIL, data.email)
                myPreferences.setValue(Constants.USER_NAMA, data.nama)
                myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
                myPreferences.setValue(Constants.USER_TELP, data.telp)
                myPreferences.setValue(Constants.USER_FOTO, data.img)
                myPreferences.setValue(Constants.USER_LEVEL, data.level)
                myPreferences.setValueInteger(Constants.USER_IDLEVEL, data.idLevel)
                startActivityEvent.value = Constants.LOGIN
            } else {
                when (val msg = response.message) {
                    "Unauthorized" -> startActivityEvent.value = "Email atau kata sandi salah"
                    "Internal server error" -> startActivityEvent.value = "Internal server error"
                    else -> startActivityEvent.value = msg
                }
            }
        }
    }
}