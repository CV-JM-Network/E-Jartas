package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.model.ProviderData
import com.jmnetwork.e_jartas.model.ProviderRequest
import com.jmnetwork.e_jartas.model.TiangData
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
    val tiangData: MutableLiveData<Map<Int, TiangData>> = MutableLiveData(mapOf())
    val totalDataProvider: MutableLiveData<Int> = MutableLiveData(0)
    val totalDataTiang: MutableLiveData<Int> = MutableLiveData(0)

    fun getProvider(limit: Int, page: Int) {
        repository.getProvider(appContext, limit, page, tokenAuth).observeForever {
            totalDataProvider.postValue(it.totalData.totalData)
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

    fun getTitikTiang(limit: Int, page: Int) {
        repository.getTitikTiang(appContext, limit, page, tokenAuth).observeForever {
            totalDataTiang.postValue(it.totalData.totalTiang)
            val currentData = tiangData.value?.toMutableMap() ?: mutableMapOf()
            val updatedData = currentData.toMutableMap()

            it.data.forEach { data ->
                if (updatedData[data.idtiang] == null) {
                    updatedData[data.idtiang] = data
                }
            }

            tiangData.postValue(updatedData)
        }
    }

    private var requestDataProvider: ProviderRequest = ProviderRequest(
        Additional(), "", ""
    )

    fun setRequestDataProvider(
        additional: Additional,
        alamat: String,
        provider: String
    ): String {
        val fields = listOf(
            alamat to "Alamat Provider",
            provider to "Nama Provider"
        )

        for ((field, message) in fields) {
            if (field.isEmpty()) return "$message tidak boleh kosong"
        }

        requestDataProvider = ProviderRequest(
            additional,
            alamat,
            provider
        )

        return ""
    }

    fun addProvider(callback: (String, String) -> Unit) {
        repository.addProvider(appContext, idAdmin, requestDataProvider, tokenAuth).observeForever {
            callback(it.status, it.message)
        }
    }

    fun blacklistProvider(idProvider: Int, isBlacklist: Boolean, callback: (String, String) -> Unit) {
        repository.blacklistProvider(appContext, idAdmin, idProvider, isBlacklist, tokenAuth).observeForever { it ->
            if (it.status == "success") {
                val currentData = providerData.value?.toMutableMap() ?: mutableMapOf()
                val updatedData = currentData.toMutableMap()

                updatedData[idProvider]?.let {
                    updatedData[idProvider] = it.copy(blackList = if (isBlacklist) "ya" else "tidak")
                }
                providerData.postValue(updatedData)
            }
            callback(it.status, it.message)
        }
    }

    fun editProvider(idProvider: Int, callback: (String, String) -> Unit) {
        repository.editProvider(appContext, idAdmin, idProvider, requestDataProvider, tokenAuth).observeForever { it ->
            if (it.status == "success") {
                val currentData = providerData.value?.toMutableMap() ?: mutableMapOf()
                val updatedData = currentData.toMutableMap()

                updatedData[idProvider]?.let {
                    updatedData[idProvider] = it.copy(
                        alamat = requestDataProvider.alamat,
                        provider = requestDataProvider.provider
                    )
                }
                providerData.postValue(updatedData)
            }
            callback(it.status, it.message)
        }
    }

    fun deleteProvider(idProvider: Int, callback: (String, String) -> Unit) {
        repository.deleteProvider(appContext, idAdmin, idProvider, tokenAuth).observeForever {
            if (it.status == "success") {
                val currentData = providerData.value?.toMutableMap() ?: mutableMapOf()
                val updatedData = currentData.toMutableMap()

                updatedData.remove(idProvider)
                providerData.postValue(updatedData)
            }
            callback(it.status, it.message)
        }
    }
}