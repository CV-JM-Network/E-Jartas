package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.ProviderData
import com.jmnetwork.e_jartas.repository.ManajemenTiangRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class ManajemenTiangViewModel(application: Application) : ViewModel() {
    private val appContext: Application = application
    private var myPreferences = MySharedPreferences(appContext)
    private var repository = ManajemenTiangRepositoryImpl()

    private val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
    private val idAdmin = myPreferences.getValueInteger(Constants.USER_IDADMIN)

    val providerData: MutableLiveData<Map<Int, ProviderData>> = MutableLiveData(mapOf())
    val totalData: MutableLiveData<Int> = MutableLiveData(0)

    fun getProvider(limit: Int, page: Int) {
        repository.getProvider(appContext, limit, page, tokenAuth).observeForever {
            totalData.postValue(it.totalData.totalData)
            val currentData = providerData.value?.toMutableMap() ?: mutableMapOf()
            val updatedData = currentData.toMutableMap()

            it.data.forEach { data ->
                if (updatedData[data.idProvider] == null) {
                    updatedData[data.idProvider] = data
                }
            }

            providerData.postValue(updatedData)
        }
    }
}