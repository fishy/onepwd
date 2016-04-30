package com.yhsif.onepwd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class HmacMd5Test {

	@Test
	public void testMd5() throws NoSuchAlgorithmException {
		String str = "asdf";
		int[] expected = {
			0x91, 0x2e, 0xc8, 0x03, 0xb2, 0xce, 0x49, 0xe4,
			0xa5, 0x41, 0x06, 0x8d, 0x49, 0x5a, 0xb5, 0x70};
		byte[] md5 = HmacMd5.md5(str.getBytes());
		assertEquals(expected.length, md5.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals((byte) expected[i], md5[i]);
		}
	}

	@Test
	public void testHmac() throws NoSuchAlgorithmException {
		String key1 = "asdf";
		String key2 = "1234567890123456789012345678901234567890123456789012345678901234567890";
		String message = "google";
		int[] expected1 = {
			0x52, 0x24, 0xa1, 0xb5, 0x60, 0xb1, 0xef, 0x5d,
			0xcf, 0x60, 0x36, 0x98, 0xb1, 0x60, 0xb5, 0x92};
		int[] expected2 = {
			0x83, 0x9d, 0xce, 0xbd, 0x94, 0xac, 0x5c, 0x8c,
			0x12, 0xb3, 0x2a, 0xfe, 0x33, 0x5e, 0x9c, 0x68};
		byte[] hmac = HmacMd5.hmac(key1, message);
		assertEquals(expected1.length, hmac.length);
		for (int i = 0; i < expected1.length; i++) {
			assertEquals((byte) expected1[i], hmac[i]);
		}
		hmac = HmacMd5.hmac(key2, message);
		assertEquals(expected2.length, hmac.length);
		for (int i = 0; i < expected2.length; i++) {
			assertEquals((byte) expected2[i], hmac[i]);
		}
	}

}
