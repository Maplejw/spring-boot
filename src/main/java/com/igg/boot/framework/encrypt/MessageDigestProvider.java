package com.igg.boot.framework.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class MessageDigestProvider {
	public static String encrypt(EncryptEnum encryptEnum, String source) {
		String algorithm = encryptEnum.getAlgorithm();
		try {
			StringBuffer sb = new StringBuffer();
			MessageDigest alga = MessageDigest.getInstance(algorithm);
			alga.update(source.getBytes());
			for (byte b : alga.digest()) {
				sb.append(String.format("%02X", b));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new HttpSystemException(HttpSystemExceptionCode.ENCRYPT_NOT_EXIST, e);
		}
	}
}
