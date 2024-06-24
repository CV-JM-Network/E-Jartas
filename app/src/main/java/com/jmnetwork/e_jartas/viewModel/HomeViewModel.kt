package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.repository.BaseRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class HomeViewModel(application: Application) : ViewModel() {
    private val appContext = application
    private var myPreferences = MySharedPreferences(appContext)
    private val repository = BaseRepositoryImpl()

    private val tokenAuth: String = myPreferences.getValue(Constants.TOKEN_AUTH).toString()

    // Get dashboard data from repository
    val dashboardData: LiveData<DasboardModel> = repository.getDashboard(appContext, tokenAuth)
}