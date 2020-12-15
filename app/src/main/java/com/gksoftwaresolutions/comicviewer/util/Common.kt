package com.gksoftwaresolutions.comicviewer.util

import androidx.fragment.app.Fragment
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Common {
    const val PAGE = 0
    const val POST_PER_PAGE = 15
    const val BASE_URL = "https://gateway.marvel.com:443"
    var INSTANCE: Fragment? = null

    //Params of query
    val PARAMS: MutableMap<String, String> = mutableMapOf()
    val CLEAN_PARAMS: MutableMap<String, String> = mutableMapOf()

    fun makeMd5HASH(timeStamp: Int, privateKey: String, publicKey: String): String {

        val mD5 = "MD5"
        val valueToMd5 = String.format("%s%s%s", timeStamp, privateKey, publicKey)
        try {
            // Create MD5 Hash
            val digest: MessageDigest = MessageDigest
                .getInstance(mD5)
            digest.update(valueToMd5.encodeToByteArray())
            val messageDigest: ByteArray = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            println(hexString.toString())
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun attachErrorHttp(e: Throwable?): String? {
        if (e is HttpException) {
            val body = e.response()!!.errorBody()
            val errorConverter: Converter<ResponseBody?, Error> =
                ServiceGenerator.retrofit!!.responseBodyConverter(
                    Error::class.java,
                    arrayOfNulls<Annotation>(0)
                )
            try {
                return errorConverter.convert(body)!!.message
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        }
        return "Request failed"
    }

    private class Error {
        var message: String? = null
    }
}