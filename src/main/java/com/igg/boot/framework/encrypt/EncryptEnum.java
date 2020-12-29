package com.igg.boot.framework.encrypt;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public enum EncryptEnum {
	MD5("MD5", "MD5"), SHA1("SHA1", "SHA-1"), HMAC("HMAC-SHA256", "SHA-256");

	EncryptEnum(String name, String algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}

	private String name;
	private String algorithm;

	public String getName() {
		return name;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public static EncryptEnum getEncryptEnum(String name) {
		for (EncryptEnum encrypt : EncryptEnum.values()) {
			if (encrypt.getName().equals(name)) {
				return encrypt;
			}
		}
		throw new HttpSystemException(HttpSystemExceptionCode.ENCRYPT_NOT_EXIST);
	}
}
