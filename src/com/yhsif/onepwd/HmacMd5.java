package com.yhsif.onepwd;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * An HMAC-MD5 implementation.
 */
public class HmacMd5 {

	public static final int BLOCK_SIZE = 64;
	public static final int HASH_SIZE = 16;
	public static final byte O_KEY_PAD = 0x5c;
	public static final byte I_KEY_PAD = 0x36;

	public static byte[] md5(byte[] message) throws NoSuchAlgorithmException {
		MessageDigest digester = MessageDigest.getInstance("MD5");
		digester.update(message);
		return digester.digest();
	}

	public static byte[] hmac(String key, String message) throws NoSuchAlgorithmException {
		Charset utf8 = null;
		try {
			utf8 = Charset.forName("UTF-8");
		} catch (java.nio.charset.UnsupportedCharsetException e) {
			// TODO: This won't happen on Android, but may happen on other platforms.
			return null;
		}
		byte keys[] = new byte[BLOCK_SIZE];
		if (key.length() > BLOCK_SIZE) {
			System.arraycopy(md5(key.getBytes(utf8)), 0, keys, 0, HASH_SIZE);
		} else {
			System.arraycopy(key.getBytes(utf8), 0, keys, 0, key.length());
		}
		byte oKeyBuf[] = new byte[BLOCK_SIZE + HASH_SIZE];
		byte iKeyBuf[] = new byte[BLOCK_SIZE + message.length()];
		for (int i = 0; i < BLOCK_SIZE; i++) {
			oKeyBuf[i] = (byte) (O_KEY_PAD ^ keys[i]);
			iKeyBuf[i] = (byte) (I_KEY_PAD ^ keys[i]);
		}
		System.arraycopy(message.getBytes(utf8), 0, iKeyBuf, BLOCK_SIZE, message.length());
		System.arraycopy(md5(iKeyBuf), 0, oKeyBuf, BLOCK_SIZE, HASH_SIZE);
		return md5(oKeyBuf);
	}

}
