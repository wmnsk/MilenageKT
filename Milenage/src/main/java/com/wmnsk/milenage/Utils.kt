package com.wmnsk.milenage

fun ByteArray.toHexString(): String {
    return this.joinToString("") {
        java.lang.String.format("%02x", it)
    }
}

fun hexStringToByte(str: String) = str.toInt(16).toByte()

fun hexStringToByteArray(str: String): ByteArray {
    val arr = ByteArray(str.length / 2)
    for (i in 0 until arr.count()) {
        val offset = i * 2
        arr[i] = hexStringToByte(str.substring(offset, offset + 2))
    }
    return arr
}

fun uShortToByteArray(v: UShort): ByteArray {
    val b = ByteArray(2)

    b[1] = (v and 0xffu).toByte()
    b[0] = ((v and 0xff00u).toInt() ushr 8).toByte()

    return b
}

fun uLongToByteArray(v: ULong): ByteArray {
    val b = ByteArray(8)

    b[7] = (v and 0xffuL).toByte()
    b[6] = ((v and 0xff00uL).toInt() ushr 8).toByte()
    b[5] = ((v and 0xff0000uL).toInt() ushr 16).toByte()
    b[4] = ((v and 0xff000000uL).toInt() ushr 24).toByte()
    b[3] = ((v and 0xff00000000uL).toInt() ushr 32).toByte()
    b[2] = ((v and 0xff0000000000uL).toInt() ushr 40).toByte()
    b[1] = ((v and 0xff000000000000uL).toInt() ushr 48).toByte()
    b[0] = ((v and 0xff00000000000000uL).toInt() ushr 56).toByte()

    return b
}
