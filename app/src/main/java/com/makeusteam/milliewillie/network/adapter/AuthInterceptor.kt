package com.makeusteam.milliewillie.network.adapter

import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val repositoryCached: RepositoryCached) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        return chain.proceed(
            originalRequest.newBuilder().apply {
                if (repositoryCached.getToken().isNotEmpty()) {
                    Log.e(repositoryCached.getToken(),"jwt")
                    header("X-ACCESS-TOKEN", repositoryCached.getToken())
                }
                url(url)
                method(originalRequest.method, originalRequest.body)
            }.build()
        )
    }
}