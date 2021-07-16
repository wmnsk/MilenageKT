package com.wmnsk.milenage

import org.junit.Test
import org.junit.Assert.*

/**
 * MilenageUnitTest is to test each function in Milenage class.
 *
 * TODO: Add more test cases that are provided by 3GPP.
 */
@ExperimentalUnsignedTypes
class MilenageUnitTest {

    @Test
    fun milenage_withDummyValues() {
        val k = "00112233445566778899aabbccddeeff"
        val op = "00112233445566778899aabbccddeeff"
        val opc = "62e75b8d6fa5bf46ec87a9276f9df54d"
        val rand = "00112233445566778899aabbccddeeff"
        val sqn = 0x000001uL
        val amf: UShort = 0x8000u
        val macA = "4af30b82a8531115"
        val macS = "23fc01ba24031362"
        val res = "700eb2300b2c4799"
        val ck = "b379874b3d183d2a21291d439e7761e1"
        val ik = "f4706f66629cf7ddf881d80025bf1255"
        val ak = "de656c8b0bce"
        val aks = "b9ac50c48a83"
        val resStar = "31b6d938a5290ccc65bc829f9820a8d9"

        val mils = arrayOf(
            Milenage(
                hexStringToByteArray(k),
                hexStringToByteArray(op),
                null, // 62e75b8d6fa5bf46ec87a9276f9df54d
                hexStringToByteArray(rand),
                sqn,
                amf
            ),
            Milenage(
                hexStringToByteArray(k),
                null, // 00112233445566778899aabbccddeeff
                hexStringToByteArray(opc),
                hexStringToByteArray(rand),
                sqn,
                amf
            )
        )

        mils.forEach { mil ->
            // init with OPc
            val opResult = if (mil.op.toHexString() == "00000000000000000000000000000000")
                "00112233445566778899aabbccddeeff" else
                 mil.op.toHexString()

            assertEquals(k, mil.k.toHexString())
            assertEquals(op, opResult)
            assertEquals(opc, mil.opc.toHexString())

            assertEquals(rand, mil.rand.toHexString())

            assertEquals("000000000001", mil.sqn.toHexString())
            assertEquals("8000", uShortToByteArray(mil.amf).toHexString())

            val macAResult = mil.f1()
            assertEquals(macA, macAResult.toHexString())
            assertEquals(macA, mil.macA.toHexString())

            val macSResult = mil.f1star()
            assertEquals(macS, macSResult.toHexString())
            assertEquals(macS, mil.macS.toHexString())

            val v2345 = mil.f2345()
            assertEquals(res, v2345[0].toHexString())
            assertEquals(ck, v2345[1].toHexString())
            assertEquals(ik, v2345[2].toHexString())
            assertEquals(ak, v2345[3].toHexString())
            assertEquals(res, mil.res.toHexString())
            assertEquals(ck, mil.ck.toHexString())
            assertEquals(ik, mil.ik.toHexString())
            assertEquals(ak, mil.ak.toHexString())

            val aksResult = mil.f5star(hexStringToByteArray(rand))
            assertEquals(aks, aksResult.toHexString())
            assertEquals(aks, mil.aks.toHexString())

            val resStarResult = mil.computeResStar("001", "01")
            assertEquals(resStar, resStarResult.toHexString())
            assertEquals(resStar, mil.resStar.toHexString())
        }
    }
}
