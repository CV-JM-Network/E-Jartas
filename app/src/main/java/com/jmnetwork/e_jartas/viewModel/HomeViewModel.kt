package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.model.DashboardResponse
import com.jmnetwork.e_jartas.retrofit.RetrofitClient
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.CustomHandler
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Response

class HomeViewModel(application: Application) : ViewModel() {
    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    val dashboardData = MutableLiveData<DasboardModel>()

    fun getDashboard() {
        myPreferences = MySharedPreferences(appContext)
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()

        RetrofitClient.apiService.dashboard(tokenAuth).enqueue(object : retrofit2.Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                when (response.code()) {
                    200 -> {
                        val data = response.body()!!.data
                        dashboardData.postValue(data)
                    }

                    500 -> {
                        CustomHandler().responseHandler(appContext, "getDashboard|onResponse", response.message(), 500)
                    }

                    else -> {
                        CustomHandler().responseHandler(appContext, "getDashboard|onResponse", response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                CustomHandler().responseHandler(appContext, "getDashboard|onFailure", t.message.toString())
            }
        })
    }

}