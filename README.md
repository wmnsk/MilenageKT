# Milenage

A MILENAGE algorithm implementation in Kotlin.

[![](https://jitpack.io/v/wmnsk/MilenageKT.svg)](https://jitpack.io/#wmnsk/MilenageKT) 
[![Kotlin Test](https://github.com/wmnsk/MilenageKT/actions/workflows/gradlew-test.yaml/badge.svg)](https://github.com/wmnsk/MilenageKT/actions/workflows/gradlew-test.yaml)

_At v0.2.0, we removed the Android-specific dependencies by re-initializing the project with gradle.
If you find any inconvenience using this library from your application, please let us know._

## Quickstart

Initialize Milenage class first with K, OP, RAND, SQN, and AMF.

On the operator side:

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

On the subscriber side:

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
    val macA = mil.f1()     // returns MAC-A in ByteArray
    val macS = mil.f1star() // returns MAC-S in ByteArray

    // `mil` stores the values, too
    mil.macA == macA // true
    mil.macS == macS // true
```

Get RES, CK, IK, AK.

```kotlin
    val v2345 = mil.f2345() // returns RES, CK, IK, AK in Array<ByteArray>

    // `mil` stores the values, too
    mil.res == v2345[0] // true
    mil.ck == v2345[1]  // true
    mil.ik == v2345[2]  // true
    mil.ak == v2345[3]  // true
```

Get 5G RES*. Note that this should be run _after_ calculating everything.

```kotlin
    val resStar = mil.computeResStar() // returns RES* in ByteArray

    // `mil` stores the values, too
    mil.resStar == resStar // true
```

## Disclaimer

This is still an experimental project. Any part of implementations(including exported APIs) may be changed before released as v1.0.0.

## Author(s)

Yoshiyuki Kurauchi ([Website](https://wmnsk.com/) / [Twitter](https://twitter.com/wmnskdmms))

_I'm always open to welcome co-authors! Please feel free to talk to me._

## LICENSE

[MIT](https://github.com/wmnsk/MilenageKT/blob/master/LICENSE)
