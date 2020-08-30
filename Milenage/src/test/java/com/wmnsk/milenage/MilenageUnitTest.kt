package com.wmnsk.milenage

import org.junit.Test
import org.junit.Assert.*

/**
 * MilenageUnitTest is to test each functions in Milenage class.
 */
class MilenageUnitTest {
    @Test
    fun milenage_initWithOP() {
        val mil = Milenage(
            hexStringToByteArray("00112233445566778899aabbccddeeff"),
            hexStringToByteArray("00112233445566778899aabbccddeeff"),
            null, // 62e75b8d6fa5bf46ec87a9276f9df54d
            hexStringToByteArray("00112233445566778899aabbccddeeff"),
            0x000001uL,
            0x8000u
        )
        assertEquals(mil.k.toHexString(), "00112233445566778899aabbccddeeff")
        assertEquals(mil.op.toHexString(), "00112233445566778899aabbccddeeff")
        assertEquals(mil.opc.toHexString(), "62e75b8d6fa5bf46ec87a9276f9df54d")

        assertEquals(mil.rand.toHexString(), "00112233445566778899aabbccddeeff")

        assertEquals(mil.sqn.toHexString(), "000000000001")
        assertEquals(uShortToByteArray(mil.amf).toHexString(), "8000")

        val maca = mil.f1()
        assertEquals(maca.toHexString(), "4af30b82a8531115")

        val macs = mil.f1star()
        assertEquals(macs.toHexString(), "23fc01ba24031362")

        val v2345 = mil.f2345()
        assertEquals(v2345[0].toHexString(), "700eb2300b2c4799")
        assertEquals(v2345[1].toHexString(), "b379874b3d183d2a21291d439e7761e1")
        assertEquals(v2345[2].toHexString(), "f4706f66629cf7ddf881d80025bf1255")
        assertEquals(v2345[3].toHexString(), "de656c8b0bce")

        val aks = mil.f5star(hexStringToByteArray("00112233445566778899aabbccddeeff"))
        assertEquals(aks.toHexString(), "b9ac50c48a83")
    }

    @Test
    fun milenage_initWithOPc() {
        val mil = Milenage(
            hexStringToByteArray("00112233445566778899aabbccddeeff"),
            null, // 00112233445566778899aabbccddeeff
            hexStringToByteArray("62e75b8d6fa5bf46ec87a9276f9df54d"),
            hexStringToByteArray("00112233445566778899aabbccddeeff"),
            0x000001uL,
            0x8000u
        )

        assertEquals(mil.k.toHexString(), "00112233445566778899aabbccddeeff")
        assertEquals(mil.opc.toHexString(), "62e75b8d6fa5bf46ec87a9276f9df54d")

        assertEquals(mil.rand.toHexString(), "00112233445566778899aabbccddeeff")

        assertEquals(mil.sqn.toHexString(), "000000000001")
        assertEquals(uShortToByteArray(mil.amf).toHexString(), "8000")

        val maca = mil.f1()
        assertEquals(maca.toHexString(), "4af30b82a8531115")

        val macs = mil.f1star()
        assertEquals(macs.toHexString(), "23fc01ba24031362")

        val v2345 = mil.f2345()
        assertEquals(v2345[0].toHexString(), "700eb2300b2c4799")
        assertEquals(v2345[1].toHexString(), "b379874b3d183d2a21291d439e7761e1")
        assertEquals(v2345[2].toHexString(), "f4706f66629cf7ddf881d80025bf1255")
        assertEquals(v2345[3].toHexString(), "de656c8b0bce")

        val aks = mil.f5star(hexStringToByteArray("00112233445566778899aabbccddeeff"))
        assertEquals(aks.toHexString(), "b9ac50c48a83")
    }
}