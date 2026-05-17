package br.com.savedra.challengecrm.data.api

import android.util.Log // CORREÇÃO: Importação faltante
import br.com.savedra.challengecrm.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // --- CORREÇÃO: URL BASE ---
    // 10.0.2.2 é o endereço especial do Android para acessar o 'localhost' do seu PC.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private fun getOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        // Interceptador para ver todos os detalhes das requisições no Logcat
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Loga corpo, headers e URL
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                val token = tokenManager.getToken()
                
                // Se houver um token salvo, ele é enviado automaticamente no cabeçalho
                if (token != null) {
                    Log.d("ApiClient", "Injetando Token JWT no cabeçalho")
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                
                val request = requestBuilder.method(original.method, original.body).build()
                chain.proceed(request)
            }
            .build()
    }

    fun getAuthApi(tokenManager: TokenManager): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(tokenManager))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    fun <T> getApi(apiClass: Class<T>, tokenManager: TokenManager): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(tokenManager))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiClass)
    }
}
