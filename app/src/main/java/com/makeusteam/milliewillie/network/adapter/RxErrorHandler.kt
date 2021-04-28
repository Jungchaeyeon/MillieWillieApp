package com.makeusteam.milliewillie.network.adapter

import com.makeusteam.milliewillie.MyApplication
import com.makeusteam.milliewillie.ext.showLongToastSafe
import com.makeusteam.milliewillie.model.ErrorResponse
import com.makeusteam.milliewillie.util.ConnectivityHelper
import com.makeusteam.milliewillie.util.GsonFactory
import com.makeusteam.milliewillie.util.Loading
import com.makeusteam.milliewillie.util.Log
import io.reactivex.exceptions.OnErrorNotImplementedException
import org.koin.android.ext.android.get
import retrofit2.HttpException
import java.net.UnknownHostException


class RxErrorHandler {
    enum class Error(val code: Int) {
        //ExpiredToken(401)
    }

    val context = MyApplication.globalApplicationContext

    fun processErrorHandler(it: Throwable?) {
        Loading.dissmiss()

        if (!context.get<ConnectivityHelper>().hasNetworkConnection()) {
            "인터넷 연결을 확인하세요.".showLongToastSafe()
            return
        }

        if (it is OnErrorNotImplementedException) {
            //Server's Response
            handleHttpException(it)
        } else {
            it?.printStackTrace()
        }
    }

    private fun handleHttpException(it: OnErrorNotImplementedException) {
        when (it.cause) {
            is HttpException -> {
                //Server's Response's code
                (it.cause as HttpException).run {
                    when (code()) {
                        else -> {
                            Log.e(getErrorResponse(this)?.message)
                        }
                    }
                }
            }
            is UnknownHostException -> "인터넷 연결을 확인하세요.".showLongToastSafe()
            else                    -> {
                Log.e(it.cause?.message)
                it.cause?.message?.showLongToastSafe()
            }
        }
    }

    private fun getErrorResponse(httpException: HttpException): ErrorResponse? {
        if (httpException.response()?.errorBody() == null) {
            return null
        }

        return GsonFactory.get().fromJson(
            httpException.response()?.errorBody()!!.charStream(), ErrorResponse::class.java
        )
    }
}