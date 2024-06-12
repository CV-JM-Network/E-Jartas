package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.UserModel
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class SettingViewModel(application: Application): ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    val userData: LiveData<UserModel> = run {
        myPreferences = MySharedPreferences(appContext)
        val liveData = MutableLiveData<UserModel>()
//        liveData.postValue(
//            UserModel(
//                myPreferences.getValue(Constants.USER_ALAMAT).toString(),
//                "",
//                myPreferences.getValue(Constants.USER_EMAIL).toString(),
//                myPreferences.getValue(Constants.USER_IDADMIN).toString().toInt(),
//                myPreferences.getValue(Constants.USER_IDLEVEL).toString().toInt(),
//                myPreferences.getValue(Constants.USER_FOTO).toString(),
//                myPreferences.getValue(Constants.USER_NAMA).toString(),
//                myPreferences.getValue(Constants.USER_TELP).toString()
//            )
//        )
        liveData
    }
}