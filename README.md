# Milenage

A MILENAGE algorithm implementation in Kotlin.

This project currently provides only a Kotlin-based Android library.
Some demo App with minimal GUI will hopefully be available in the future...?

## Quickstart

Initialize Milenage class first with K, OP, RAND, SQN, and AMF.

On the operator(HSS) side:

```kotlin
    val mil = Milenage(
        hexStringToByteArray("00112233445566778899aabbccddeeff"),
        hexStringToByteArray("00112233445566778899aabbccddeeff"),
        null, // 62e75b8d6fa5bf46ec87a9276f9df54d
        hexStringToByteArray("00112233445566778899aabbccddeeff"),
        0x000001uL,
        0x8000u
    )
```

On the subscriber(UE) side:

```kotlin
    val mil = Milenage(
        hexStringToByteArray("00112233445566778899aabbccddeeff"),
        null, // 00112233445566778899aabbccddeeff
        hexStringToByteArray("62e75b8d6fa5bf46ec87a9276f9df54d"),
        hexStringToByteArray("00112233445566778899aabbccddeeff"),
        0x000001uL,
        0x8000u
    )
```

Get MAC-A and MAC-S.

```kotlin
    val maca = mil.f1() // returns MAC-A in ByteArray
    val macs = mil.f1star() // returns MAC-S in ByteArray
```

Get RES, CK, IK, AK.

```kotlin
    val v2345 = mil.f2345() // returns RES, CK, IK, AK in Array<ByteArray>
```

## Disclaimer

This is still an experimental project. Any part of implementations(including exported APIs) may be changed before released as v1.0.0.

## Author(s)

Yoshiyuki Kurauchi ([Website](https://wmnsk.com/) / ([Twitter](https://twitter.com/wmnskdmms))

_I'm always open to welcome co-authors! Please feel free to talk to me._

## LICENSE

[MIT](https://github.com/wmnsk/MilenageKT/blob/master/LICENSE)
