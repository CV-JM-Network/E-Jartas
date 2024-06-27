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
                when (response.code()) {
                    200 -> {
                        spinnerData.postValue(response.body())
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "getSpinnerData|onResponse", response.message(), 500)
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "getSpinnerData|onResponse", response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<SpinnerResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "getSpinnerData|onFailure", t.message.toString())
            }
        })

        return spinnerData
    }

    override fun getRuasJalan(
        context: Context, limit: Int, page: Int, tabel: String, tokenAuth: String
    ): LiveData<RuasJalanResponse> {
        val ruasJalanData = MutableLiveData<RuasJalanResponse>()

        apiService.getRuasJalan(limit, page, tabel, tokenAuth).enqueue(object : retrofit2.Callback<RuasJalanResponse> {
            override fun onResponse(call: Call<RuasJalanResponse>, response: Response<RuasJalanResponse>) {
                when (response.code()) {
                    200 -> {
                        ruasJalanData.postValue(response.body())
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "getRuasJalan|onResponse", response.message(), 500)
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "getRuasJalan|onResponse", response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<RuasJalanResponse>, t: Throwable) {
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
                when (response.code()) {
                    200 -> {
                        result.postValue(DefaultResponse(response.body()!!.message, response.body()!!.status))
                    }

                    400 -> {
                        CustomHandler().responseHandler(context, "addRuasJalan|onResponse", response.message(), 400)
                        result.postValue(DefaultResponse(response.message(), "bad_request"))
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "addRuasJalan|onResponse", response.message(), 500)
                        result.postValue(DefaultResponse(response.message(), "internal_server_error"))
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "addRuasJalan|onResponse", response.message(), response.code())
                        result.postValue(DefaultResponse(response.message(), "error"))
                    }
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
        context: Context, idadmin: Int, iddata: String, requestData: RuasJalanRequest, tokenAuth: String
    ): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val dataJson = Gson().toJson(requestData)

        apiService.ruasJalan("edit", iddata, JSONObject(dataJson), idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                when (response.code()) {
                    200 -> {
                        result.postValue(DefaultResponse(response.body()!!.message, response.body()!!.status))
                    }

                    400 -> {
                        CustomHandler().responseHandler(context, "editRuasJalan|onResponse", response.message(), 400)
                        result.postValue(DefaultResponse(response.message(), "bad_request"))
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "editRuasJalan|onResponse", response.message(), 500)
                        result.postValue(DefaultResponse(response.message(), "internal_server_error"))
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "editRuasJalan|onResponse", response.message(), response.code())
                        result.postValue(DefaultResponse(response.message(), "error"))
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "editRuasJalan|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun deleteRuasJalan(context: Context, idadmin: Int, iddata: String, tokenAuth: String): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()

        apiService.ruasJalan("delete", iddata, null, idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                when (response.code()) {
                    200 -> {
                        result.postValue(DefaultResponse(response.body()!!.message, response.body()!!.status))
                    }

                    400 -> {
                        CustomHandler().responseHandler(context, "deleteRuasJalan|onResponse", response.message(), 400)
                        result.postValue(DefaultResponse(response.message(), "bad_request"))
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "deleteRuasJalan|onResponse", response.message(), 500)
                        result.postValue(DefaultResponse(response.message(), "internal_server_error"))
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "deleteRuasJalan|onResponse", response.message(), response.code())
                        result.postValue(DefaultResponse(response.message(), "error"))
                    }
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