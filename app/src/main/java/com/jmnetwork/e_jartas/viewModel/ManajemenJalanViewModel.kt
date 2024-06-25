package com.jmnetwork.e_jartas.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.repository.BaseRepositoryImpl
import com.jmnetwork.e_jartas.utils.Constants
import com.jmnetwork.e_jartas.utils.MySharedPreferences

class ManajemenJalanViewModel(appContext: Application) : ViewModel() {
    private val appContext: Application = appContext
    private var myPreferences = MySharedPreferences(appContext)
    private val repository = BaseRepositoryImpl()

    private val tokenAuth: String = myPreferences.getValue(Constants.TOKEN_AUTH).toString()

    private val _ruasJalanData: LiveData<RuasJalanResponse> = repository.getRuasJalan(appContext, 10, 1, "ruas_jalan", tokenAuth)
    val ruasJalanData: MutableLiveData<RuasJalanResponse> get() = _ruasJalanData as MutableLiveData<RuasJalanResponse>

    fun getRuasJalan(limit: Int, page: Int) {
        repository.getRuasJalan(appContext, limit, page, "ruas_jalan", tokenAuth).observeForever {
            ruasJalanData.postValue(it)
        }
    }
}