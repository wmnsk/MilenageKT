package com.wmnsk.milenage

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import kotlin.experimental.xor

@ExperimentalUnsignedTypes
class Milenage @ExperimentalUnsignedTypes constructor(
    val k: ByteArray,
    op: ByteArray?,
    opc: ByteArray?,
    var rand: ByteArray,
    sqn: ULong,
    var amf: UShort
) {
    val op: ByteArray
    var opc: ByteArray

    var sqn: ByteArray

    var macA: ByteArray = ByteArray(0)
    var macS: ByteArray = ByteArray(0)

    var res: ByteArray = ByteArray(0)
    var ck: ByteArray = ByteArray(0)
    var ik: ByteArray = ByteArray(0)
    var ak: ByteArray = ByteArray(0)
    var aks: ByteArray = ByteArray(0)
    var resStar: ByteArray = ByteArray(0)

    init {
        require(op != null || opc != null) { "Either of op or opc should be given." }

        //this.opc = ByteArray(16)

        if (op != null) {
            this.op = op
            this.opc = computeOPc()
        } else { // opc should be set
            this.op = ByteArray(16)
            this.opc = opc!!
        }

        this.sqn = uLongToByteArray(sqn).copyOfRange(2, 8)
    }

    private fun f1base(): ByteArray {
        val rijndaelInput = ByteArray(16)
        for (i in 0 until 16) {
            rijndaelInput[i] = rand[i] xor this.opc[i]
        }

        val temp: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }

        val in1 = ByteArray(16)
        for (i in 0 until 6) {
            in1[i] = this.sqn[i]
            in1[i + 8] = this.sqn[i]
        }

        val amfData = uShortToByteArray(this.amf)
        for (i in 0 until 2) {
            in1[i + 6] = amfData[i]
            in1[i + 14] = amfData[i]
        }

        // XOR op_c and in1, rotate by r1=64, and XOR
        // on the constant c1 (which is all zeroes)
        for (i in 0 until 16) {
            rijndaelInput[(i + 8) % 16] = in1[i] xor this.opc[i]
        }

        /* XOR on the value temp computed before */
        for (i in 0 until 16) {
            rijndaelInput[i] = (rijndaelInput[i] xor temp[i])
        }

        val out: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }
        return xor(out, this.opc)
    }

    private fun computeOPc(): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        val k = SecretKeySpec(this.k, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, k)
        val cipherText: ByteArray = cipher.doFinal(this.op)

        val opc = xor(cipherText , this.op)
        this.opc = opc
        return opc
    }

    fun f1(): ByteArray {
        val mac: ByteArray = try {
            this.f1base()
        } catch (e: Throwable) {
            throw e
        }

        val macA = ByteArray(8)
        for (i in 0 until 8) {
            macA[i] = mac[i]
        }
        this.macA = macA
        return macA
    }

    fun f1star(): ByteArray {
        val mac: ByteArray = try {
            this.f1base()
        } catch (e: Throwable) {
            throw e
        }

        val macS = ByteArray(8)
        for (i in 8 until 16) {
            macS[i-8] = mac[i]
        }
        this.macS = macS
        return macS
    }

    fun f2345(): Array<ByteArray> {
        val rijndaelInput = ByteArray(16)
        for (i in 0 until 16) {
            rijndaelInput[i] = rand[i] xor this.opc[i]
        }

        val temp: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }

        // To obtain output block OUT2: XOR OPc and TEMP, rotate by r2=0, and XOR on the
        // constant c2 (which is all zeroes except that the last bit is 1).
        for (i in 0 until 16) {
            rijndaelInput[i] = temp[i] xor this.opc[i]
        }
        rijndaelInput[15] = (rijndaelInput[15] xor 1)

        var out: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }
        out = xor(out, this.opc)

        val res = ByteArray(8)
        for (i in 0 until 8) {
            res[i] = out[i + 8]
        }
        this.res = res

        val ak = ByteArray(6)
        for (i in 0 until 6) {
            ak[i] = out[i]
        }
        this.ak = ak

        // To obtain output block OUT3: XOR OPc and TEMP, rotate by r3=32, and XOR on the
        // constant c3 (which is all zeroes except that the next to last bit is 1).
        for (i in 0 until 16) {
            rijndaelInput[(i + 12) % 16] = temp[i] xor this.opc[i]
        }
        rijndaelInput[15] = (rijndaelInput[15] xor 2)

        val ck = ByteArray(16)
        out = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }

        out = xor(out, this.opc)
        for (i in 0 until 16) {
            ck[i] = out[i]
        }
        this.ck = ck

        // To obtain output block OUT4: XOR OPc and TEMP, rotate by r4=64, and XOR on the
        // constant c4 (which is all zeroes except that the 2nd from last bit is 1).
        for (i in 0 until 16) {
            rijndaelInput[(i + 8) % 16] = temp[i] xor this.opc[i]
        }
        rijndaelInput[15] = (rijndaelInput[15] xor 4)

        val ik = ByteArray(16)
        out = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }

        out = xor(out, this.opc)
        for (i in 0 until 16) {
            ik[i] = out[i]
        }
        this.ik = ik

        return arrayOf(res, ck, ik, ak)
    }

    fun f5star(rand: ByteArray): ByteArray {
        val rijndaelInput = ByteArray(16)
        for (i in 0 until 16) {
            rijndaelInput[i] = rand[i] xor this.opc[i]
        }

        val temp: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }

        // To obtain output block OUT5: XOR OPc and TEMP, rotate by r5=96, and XOR on the
        // constant c5 (which is all zeroes except that the 3rd from last bit is 1).
        for (i in 0 until 16) {
            rijndaelInput[(i + 4) % 16] = temp[i] xor this.opc[i]
        }
        rijndaelInput[15] = (rijndaelInput[15] xor 8)

        var out: ByteArray = try {
            encrypt(this.k, rijndaelInput)
        } catch (e: Throwable) {
            throw e
        }
        out = xor(out, this.opc)

        val aks = ByteArray(6)
        for (i in 0 until 6) {
            aks[i] = out[i]
        }
        this.aks = aks

        return aks
    }

    fun computeResStar(mcc: String, mnc: String): ByteArray {
        if (mcc.length != 3) {
            throw IllegalArgumentException("invalid MCC: %s".format(mcc))
        }

        val n = when {
            mnc.length == 2 -> {
                "0$mnc"
            }
            mnc.length != 3 -> {
                throw IllegalArgumentException("invalid MNC: %s".format(mnc))
            }
            else -> {
                mnc
            }
        }

        val snn = "5G:mnc%s.mcc%s.3gppnetwork.org".format(n, mcc).toByteArray()

        var b = ByteArray(1)
        b[0] = 0x6b

        b += snn
        b += uShortToByteArray(0x20u.toUShort())

        b += this.rand
        b += uShortToByteArray(0x10u.toUShort())

        b += this.res
        b += uShortToByteArray(0x08u.toUShort())

        val k = this.ck + this.ik

        val algo = "HmacSHA256"
        val mac = Mac.getInstance(algo)
        val spec = SecretKeySpec(k, algo)
        mac.init(spec)

        val out = mac.doFinal(b)
        this.resStar = out.copyOfRange(16, out.size)
        return this.resStar
    }
}

fun encrypt(key: ByteArray, plain: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    val k = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, k)
    return cipher.doFinal(plain)
}

fun xor(b1: ByteArray, b2: ByteArray): ByteArray {
    val l: Int = if (b1.size - b2.size < 0) {
        b1.size
    } else {
        b2.size
    }

    for (i in 0 until l) {
        b1[i] = (b1[i] xor b2[i])
    }
    return b1
}
