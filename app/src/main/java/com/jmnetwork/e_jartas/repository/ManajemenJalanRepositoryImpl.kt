package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.model.SpinnerResponse
import com.jmnetwork.e_jartas.utils.CustomHandler
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ManajemenJalanRepositoryImpl : ManajemenJalanRepository {
    private val apiService = RetrofitClient.apiService

    override fun getSpinnerData(
        context: Context, varian: String, tokenAuth: String
    ): LiveData<SpinnerResponse> {
        val spinnerData = MutableLiveData<SpinnerResponse>()

        apiService.getSpinnerData(varian, tokenAuth).enqueue(object : retrofit2.Callback<SpinnerResponse> {
            override fun onResponse(call: Call<SpinnerResponse>, response: Response<SpinnerResponse>) {
                if (response.isSuccessful) {
                    spinnerData.postValue(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                    val status = when (response.code()) {
                        400 -> "bad_request"
                        500 -> "internal_server_error"
                        else -> "error"
                    }
                    CustomHandler().responseHandler(context, "getSpinnerData|onResponse", errorMsg, response.code())
                }
            }

            override fun onFailure(call: Call<SpinnerResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "getSpinnerData|onFailure", t.message.toString())
            }
        })

        return spinnerData
    }

    override fun getRuasJalan(
        context: Context, limit: Int, page: Int, tokenAuth: String
    ): LiveData<RuasJalanResponse> {
        val ruasJalanData = MutableLiveData<RuasJalanResponse>()

        apiService.getAllData(limit, page, "ruas_jalan", tokenAuth).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    val data = Gson().fromJson(responseString, RuasJalanResponse::class.java)
                    ruasJalanData.postValue(data)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") }!!
                    CustomHandler().responseHandler(context, "getRuasJalan|onResponse", errorMsg, response.code())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CustomHandler().responseHandler(context, "getRuasJalan|onFailure", t.message.toString())
            }
        })

        return ruasJalanData
    }

    override fun addRuasJalan(
        context: Context, idadmin: Int, requestData: RuasJalanRequest, tokenAuth: String
    ): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val dataJson = Gson().toJson(requestData)

        apiService.ruasJalan("add", null, JSONObject(dataJson), idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(DefaultResponse(it.message, it.status))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                    val status = when (response.code()) {
                        400 -> "bad_request"
                        500 -> "internal_server_error"
                        else -> "error"
                    }
                    CustomHandler().responseHandler(context, "addRuasJalan|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "addRuasJalan|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun editRuasJalan(
        context: Context, idadmin: Int, iddata: Int, requestData: RuasJalanRequest, tokenAuth: String
    ): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val dataJson = Gson().toJson(requestData)

        apiService.ruasJalan("edit", iddata, JSONObject(dataJson), idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(DefaultResponse(it.message, it.status))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                    val status = when (response.code()) {
                        400 -> "bad_request"
                        500 -> "internal_server_error"
                        else -> "error"
                    }
                    CustomHandler().responseHandler(context, "editRuasJalan|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "editRuasJalan|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun deleteRuasJalan(context: Context, idadmin: Int, iddata: Int, tokenAuth: String): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()

        apiService.ruasJalan("delete", iddata, null, idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(DefaultResponse(it.message, it.status))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                    val status = when (response.code()) {
                        400 -> "bad_request"
                        500 -> "internal_server_error"
                        else -> "error"
                    }
                    CustomHandler().responseHandler(context, "deleteRuasJalan|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "deleteRuasJalan|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }
}