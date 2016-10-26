package com.yhsif.onepwd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class HmacMd5Test {

  @Test
  public void testMd5() throws NoSuchAlgorithmException {
    String str = "asdf";
    byte[] expected = {
      (byte) 0x91, (byte) 0x2e, (byte) 0xc8, (byte) 0x03,
      (byte) 0xb2, (byte) 0xce, (byte) 0x49, (byte) 0xe4,
      (byte) 0xa5, (byte) 0x41, (byte) 0x06, (byte) 0x8d,
      (byte) 0x49, (byte) 0x5a, (byte) 0xb5, (byte) 0x70};
    byte[] md5 = HmacMd5.md5(str.getBytes());
    assertThat(md5).isEqualTo(expected);
  }

  @Test
  public void testHmac() throws NoSuchAlgorithmException {
    String key1 = "asdf";
    String key2 =
        "1234567890123456789012345678901234567890123456789012345678901234567890";
    String message = "google";
    byte[] expected1 = {
        (byte) 0x52, (byte) 0x24, (byte) 0xa1, (byte) 0xb5,
        (byte) 0x60, (byte) 0xb1, (byte) 0xef, (byte) 0x5d,
        (byte) 0xcf, (byte) 0x60, (byte) 0x36, (byte) 0x98,
        (byte) 0xb1, (byte) 0x60, (byte) 0xb5, (byte) 0x92};
    byte[] expected2 = {
        (byte) 0x83, (byte) 0x9d, (byte) 0xce, (byte) 0xbd,
        (byte) 0x94, (byte) 0xac, (byte) 0x5c, (byte) 0x8c,
        (byte) 0x12, (byte) 0xb3, (byte) 0x2a, (byte) 0xfe,
        (byte) 0x33, (byte) 0x5e, (byte) 0x9c, (byte) 0x68};
    byte[] hmac = HmacMd5.hmac(key1, message);
    assertThat(hmac).isEqualTo(expected1);
    hmac = HmacMd5.hmac(key2, message);
    assertThat(hmac).isEqualTo(expected2);
  }

}
