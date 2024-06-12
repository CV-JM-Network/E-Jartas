package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class ScanQrViewModel(application: Application): ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

}