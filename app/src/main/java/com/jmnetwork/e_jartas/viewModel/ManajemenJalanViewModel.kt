package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.Location
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.repository.BaseRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences
import org.json.JSONObject

class ManajemenJalanViewModel(appContext: Application) : ViewModel() {
    private val appContext: Application = appContext
    private var myPreferences = MySharedPreferences(appContext)
    private val repository = BaseRepositoryImpl()

    private val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
    private val idAdmin = myPreferences.getValueInteger(Constants.USER_IDADMIN)

    //    private val _ruasJalanData: LiveData<RuasJalanResponse> = repository.getRuasJalan(appContext, 10, 1, "ruas_jalan", tokenAuth)
    val ruasJalanData: MutableLiveData<RuasJalanResponse> = MutableLiveData()

    val kecamatanSpinner = repository.getSpinnerData(appContext, "kecamatan", tokenAuth)
    val desaSpinner = repository.getSpinnerData(appContext, "desa", tokenAuth)
    val statusSpinner = repository.getSpinnerData(appContext, "status", tokenAuth)
    val tipeSpinner = repository.getSpinnerData(appContext, "tipe", tokenAuth)
    val fungsiSpinner = repository.getSpinnerData(appContext, "fungsi", tokenAuth)

    fun getRuasJalan(limit: Int, page: Int) {
        repository.getRuasJalan(appContext, limit, page, "ruas_jalan", tokenAuth).observeForever {
            ruasJalanData.postValue(it)
        }
    }

    private var addRuasJalanRequest: RuasJalanRequest = RuasJalanRequest(
        "", "", "", "", "", "", "", "", emptyList(), emptyList()
    )

    fun setLoginRequest(
        noRuas: String,
        namaRuasJalan: String,
        desa: String,
        kecamatan: String,
        panjang: String,
        status: String,
        tipe: String,
        fungsi: String,
        latlong: List<Location>,
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

        addRuasJalanRequest = RuasJalanRequest(
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
        repository.addRuasJalan(appContext, idAdmin, addRuasJalanRequest, tokenAuth).observeForever {
            callback(it.status, it.message)
        }
    }
}