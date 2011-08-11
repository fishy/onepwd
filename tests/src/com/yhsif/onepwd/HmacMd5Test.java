package com.yhsif.onepwd;

import junit.framework.TestCase;

import java.security.NoSuchAlgorithmException;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.yhsif.onepwd.HmacMd5Test \
 * com.yhsif.onepwd.tests/android.test.InstrumentationTestRunner
 */
public class HmacMd5Test extends TestCase {

	public void testMd5() throws NoSuchAlgorithmException {
		String str = "asdf";
		int[] expected = {
			0x91, 0x2e, 0xc8, 0x03, 0xb2, 0xce, 0x49, 0xe4,
			0xa5, 0x41, 0x06, 0x8d, 0x49, 0x5a, 0xb5, 0x70};
		byte[] md5 = HmacMd5.md5(str.getBytes());
		for (int i = 0; i < expected.length; i++) {
			assertEquals((byte) expected[i], md5[i]);
		}
	}

	public void testHmac() throws NoSuchAlgorithmException {
		String key = "asdf";
		String message = "google";
		int[] expected = {
			0x52, 0x24, 0xa1, 0xb5, 0x60, 0xb1, 0xef, 0x5d,
			0xcf, 0x60, 0x36, 0x98, 0xb1, 0x60, 0xb5, 0x92};
		byte[] hmac = HmacMd5.hmac(key, message);
		for (int i = 0; i < expected.length; i++) {
			assertEquals((byte) expected[i], hmac[i]);
		}
	}

}
