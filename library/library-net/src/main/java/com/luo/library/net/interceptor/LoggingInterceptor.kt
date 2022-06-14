package com.luo.library.net.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import kotlin.jvm.Throws

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val t1 = System.nanoTime()
        Log.v(
            TAG,
            """ \n
                **********************************
                * Sending request: ${request.url}
                * ${request.headers}
                **********************************
            """.trimIndent()
        )
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.v(
            TAG,
            """\n
                **********************************
                * Received response for: ${response.request.url} in ${(t2 - t1) / 1e6} ms
                * ${request.headers}
                **********************************         
            """.trimIndent()
        )
        return response
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }

}