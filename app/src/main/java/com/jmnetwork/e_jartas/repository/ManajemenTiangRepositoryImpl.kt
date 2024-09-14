package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jmnetwork.e_jartas.model.Additional
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.ProviderRequest
import com.jmnetwork.e_jartas.model.ProviderResponse
import com.jmnetwork.e_jartas.utils.AdditionalDeserializer
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
                    val gson = GsonBuilder().registerTypeAdapter(Additional::class.java, AdditionalDeserializer()).create()
                    val responseString = response.body()?.string()
                    val data = gson.fromJson(responseString, ProviderResponse::class.java)
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

    override fun addProvider(
        context: Context, idadmin: Int, requestData: ProviderRequest, tokenAuth: String
    ): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val dataJson = Gson().toJson(requestData)

        apiService.provider("add", null, JSONObject(dataJson), idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
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
                    CustomHandler().responseHandler(context, "addProvider|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "addProvider|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun blacklistProvider(context: Context, idadmin: Int, iddata: Int, isBlacklist: Boolean, tokenAuth: String): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val blacklistStatus = if (isBlacklist) "ya" else "tidak"

        apiService.blacklistProvider(idadmin, iddata, blacklistStatus, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
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
                    CustomHandler().responseHandler(context, "blacklistProvider|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "blacklistProvider|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun editProvider(context: Context, idadmin: Int, iddata: Int, requestData: ProviderRequest, tokenAuth: String): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()
        val dataJson = Gson().toJson(requestData)

        apiService.provider("edit", iddata, JSONObject(dataJson), idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
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
                    CustomHandler().responseHandler(context, "editProvider|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "editProvider|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }

    override fun deleteProvider(context: Context, idadmin: Int, iddata: Int, tokenAuth: String): LiveData<DefaultResponse> {
        val result = MutableLiveData<DefaultResponse>()

        apiService.provider("delete", iddata, null, idadmin, tokenAuth).enqueue(object : retrofit2.Callback<DefaultResponse> {
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
                    CustomHandler().responseHandler(context, "deleteProvider|onResponse", errorMsg, response.code())
                    result.postValue(DefaultResponse(response.message(), status))
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "deleteProvider|onFailure", t.message.toString())
                result.postValue(DefaultResponse(t.message.toString(), "failure"))
            }
        })

        return result
    }
}