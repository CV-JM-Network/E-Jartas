package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.model.RuasJalanData
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.model.SpinnerResponse
import com.jmnetwork.e_jartas.repository.ManajemenJalanRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import org.json.JSONObject

class ManajemenJalanViewModel(application: Application) : ViewModel() {
    private val appContext: Application = application
    private var myPreferences = MySharedPreferences(appContext)
    private val repository = ManajemenJalanRepositoryImpl()

    private val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
    private val idAdmin = myPreferences.getValueInteger(Constants.USER_IDADMIN)

    val ruasJalanData: MutableLiveData<Map<Int, RuasJalanData>> = MutableLiveData(mapOf())
    val totalData: MutableLiveData<Int> = MutableLiveData(0)
    var kecamatanSpinner = MutableLiveData<SpinnerResponse>()
    var desaSpinner = MutableLiveData<SpinnerResponse>()
    var statusSpinner = MutableLiveData<SpinnerResponse>()
    var tipeSpinner = MutableLiveData<SpinnerResponse>()
    var fungsiSpinner = MutableLiveData<SpinnerResponse>()

    fun getRuasJalan(limit: Int, page: Int) {
        repository.getRuasJalan(appContext, limit, page, tokenAuth).observeForever {
            totalData.postValue(it.totalData.totalData)
            val currentData = ruasJalanData.value?.toMutableMap() ?: mutableMapOf()
            val updatedData = currentData.toMutableMap()

            it.data.forEach { data ->
                if (updatedData[data.idRuasJalan] == null) {
                    updatedData[data.idRuasJalan] = data
                }
            }

            ruasJalanData.postValue(updatedData)
        }
    }

    init {
        getSpinnerData()
    }

    fun getSpinnerData() {
        fetchSpinnerData("kecamatan", kecamatanSpinner)
        fetchSpinnerData("desa", desaSpinner)
        fetchSpinnerData("status", statusSpinner)
        fetchSpinnerData("tipe", tipeSpinner)
        fetchSpinnerData("fungsi", fungsiSpinner)
    }

    private var requestData: RuasJalanRequest = RuasJalanRequest(
        0, "", "", "", "", "", "", "", "", Location(), emptyList()
    )

    private fun fetchSpinnerData(spinnerType: String, spinnerLiveData: MutableLiveData<SpinnerResponse>) {
        repository.getSpinnerData(appContext, spinnerType, tokenAuth).observeForever {
            spinnerLiveData.postValue(it)
        }
    }

    fun setRequestData(
        idRuasJalan: Int,
        noRuas: String,
        namaRuasJalan: String,
        desa: String,
        kecamatan: String,
        panjang: String,
        status: String,
        tipe: String,
        fungsi: String,
        latlong: Location,
        additional: List<JSONObject>
    ): String {
        val fields = listOf(
            noRuas to "No Ruas Jalan",
            namaRuasJalan to "Nama Ruas Jalan",
            desa to "Desa",
            kecamatan to "Kecamatan",
            panjang to "Panjang",
            status to "Status",
            fungsi to "Fungsi",
            tipe to "Tipe",
        )

        for ((field, message) in fields) {
            if (field.isEmpty()) return "$message tidak boleh kosong"
        }

        if (latlong.isEmpty()) return "Latlong tidak boleh kosong"

        requestData = RuasJalanRequest(
            idRuasJalan,
            noRuas,
            namaRuasJalan,
            desa,
            kecamatan,
            panjang,
            status,
            tipe,
            fungsi,
            latlong,
            additional
        )

        return ""
    }


    fun addRuasJalan(callback: (String, String) -> Unit) {
        repository.addRuasJalan(appContext, idAdmin, requestData, tokenAuth).observeForever {
            callback(it.status, it.message)
        }
    }

    fun editRuasJalan(idruas: Int, callback: (String, String) -> Unit) {
        repository.editRuasJalan(appContext, idAdmin, idruas, requestData, tokenAuth).observeForever {
            if (it.status == "success") {
                // Use Transformations.map to create a new LiveData with the updated list
                ruasJalanData.value?.let { currentData ->
                    val updatedData = currentData.toMutableMap().let { mutableMap ->
                        mutableMap[idruas] = requestData.toRuasJalanData(idAdmin, requestData)
                        mutableMap.toMap() // Convert back to immutable map
                    }
                    ruasJalanData.postValue(updatedData)
                }
            }
            callback(it.status, it.message)
        }
    }

    fun deleteRuasJalan(idruas: Int, callback: (String, String) -> Unit) {
        repository.deleteRuasJalan(appContext, idAdmin, idruas, tokenAuth).observeForever {
            if (it.status == "success") {
                // Use Transformations.map to create a new LiveData with the updated list
                ruasJalanData.value?.let { currentData ->
                    val updatedData = currentData.toMutableMap().let { mutableMap ->
                        mutableMap.remove(idruas)
                        mutableMap.toMap() // Convert back to immutable map
                    }
                    ruasJalanData.postValue(updatedData)
                }
            }
            callback(it.status, it.message)
        }
    }
}