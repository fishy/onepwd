package com.yhsif.onepwd

import java.security.MessageDigest

/**
 * An HMAC-MD5 implementation.
 */
object HmacMd5 {
    const val BLOCK_SIZE = 64
    const val HASH_SIZE = 16
    const val O_KEY_PAD: Byte = 0x5c
    const val I_KEY_PAD: Byte = 0x36

    fun md5(message: ByteArray): ByteArray {
        MessageDigest.getInstance("MD5").let { digester ->
            digester.update(message)
            return digester.digest()
        }
    }

    fun hmac(key: String, message: String): ByteArray {
        val keys = ByteArray(BLOCK_SIZE)
        if (key.length > BLOCK_SIZE) {
            System.arraycopy(md5(key.toByteArray()), 0, keys, 0, HASH_SIZE)
        } else {
            System.arraycopy(key.toByteArray(), 0, keys, 0, key.length)
        }
        val oKeyBuf = ByteArray(BLOCK_SIZE + HASH_SIZE)
        val iKeyBuf = ByteArray(BLOCK_SIZE + message.length)
        for (i in 0 until BLOCK_SIZE) {
            oKeyBuf[i] = (O_KEY_PAD.toInt() xor keys[i].toInt()).toByte()
            iKeyBuf[i] = (I_KEY_PAD.toInt() xor keys[i].toInt()).toByte()
        }
        System.arraycopy(
            message.toByteArray(),
            0,
            iKeyBuf,
            BLOCK_SIZE,
            message.length
        )
        System.arraycopy(md5(iKeyBuf), 0, oKeyBuf, BLOCK_SIZE, HASH_SIZE)
        return md5(oKeyBuf)
    }
}
