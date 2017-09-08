package com.yhsif.onepwd

import com.google.common.truth.Truth.assertThat

import org.junit.Test

class HmacMd5Test {
  @Test
  fun testMd5() {
    val str = "asdf".toByteArray()
    val expected = byteArrayOf(
      0x91.toByte(), 0x2e.toByte(), 0xc8.toByte(), 0x03.toByte(),
      0xb2.toByte(), 0xce.toByte(), 0x49.toByte(), 0xe4.toByte(),
      0xa5.toByte(), 0x41.toByte(), 0x06.toByte(), 0x8d.toByte(),
      0x49.toByte(), 0x5a.toByte(), 0xb5.toByte(), 0x70.toByte())
    val md5 = HmacMd5.md5(str)
    assertThat(md5).isEqualTo(expected);
  }

  @Test
  fun testHmac() {
    val key1 = "asdf"
    val key2 =
        "1234567890123456789012345678901234567890123456789012345678901234567890"
    val message = "google"
    val expected1 = byteArrayOf(
        0x52.toByte(), 0x24.toByte(), 0xa1.toByte(), 0xb5.toByte(),
        0x60.toByte(), 0xb1.toByte(), 0xef.toByte(), 0x5d.toByte(),
        0xcf.toByte(), 0x60.toByte(), 0x36.toByte(), 0x98.toByte(),
        0xb1.toByte(), 0x60.toByte(), 0xb5.toByte(), 0x92.toByte())
    val expected2 = byteArrayOf(
        0x83.toByte(), 0x9d.toByte(), 0xce.toByte(), 0xbd.toByte(),
        0x94.toByte(), 0xac.toByte(), 0x5c.toByte(), 0x8c.toByte(),
        0x12.toByte(), 0xb3.toByte(), 0x2a.toByte(), 0xfe.toByte(),
        0x33.toByte(), 0x5e.toByte(), 0x9c.toByte(), 0x68.toByte())
    val hmac1 = HmacMd5.hmac(key1, message)
    assertThat(hmac1).isEqualTo(expected1)
    val hmac2 = HmacMd5.hmac(key2, message)
    assertThat(hmac2).isEqualTo(expected2);
  }
}
