package com.example.daggerhilttest.models.v2
import android.util.Log
import com.example.daggerhilttest.util.Resource_v2
import retrofit2.Response
abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource_v2<T> {
        try {
            val response = apiCall()
            Log.d("weather_api_call_v2",response.body().toString())
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return Resource_v2.Success(body)
                }
            }
            return Resource_v2.Error(response.message().toString())
        } catch (e: Exception) {
            Log.d("weather_api_call_v2",e.toString())
            return Resource_v2.Error(e.message)
        }
    }
}