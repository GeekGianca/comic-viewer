package com.gksoftwaresolutions.comicviewer

import org.junit.Test
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun md5Hash() {
        val mD5 = "MD5"
        val valueToMd5 = String.format(
            "%s%s%s",
            400,
            "5bd506dff487f8ff10498eb76df7915e6f096c9b",
            "f99407ffc8f82e99d16d83ab41bf2288"
        )
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
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
}