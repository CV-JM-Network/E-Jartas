package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.ProviderResponse
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.utils.CustomHandler
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ManajemenTiangRepositoryImpl : ManajemenTiangRepository {
    private val apiService = RetrofitClient.apiService

    override fun getProvider(
        context: Context, limit: Int, page: Int, tokenAuth: String
    ): LiveData<ProviderResponse> {
        val providerData = MutableLiveData<ProviderResponse>()

        apiService.getAllData(limit, page, "provider", tokenAuth).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    val data = Gson().fromJson(responseString, ProviderResponse::class.java)
                    providerData.postValue(data)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                    CustomHandler().responseHandler(context, "getRuasJalan|onResponse", errorMsg, response.code())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CustomHandler().responseHandler(context, "getProviderData|onFailure", t.message.toString())
            }
        })

        return providerData
    }

    override fun addProvider(context: Context, idadmin: Int, requestData: RuasJalanRequest, tokenAuth: String): LiveData<DefaultResponse> {
        TODO("Not yet implemented")
    }

}