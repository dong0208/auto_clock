package com.yang.auto.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lww
 * @date 2019-04-12 11:27 PM
 */
public class EncodeUtil {

	public static String md5Encode(String password) {
		return DigestUtils.md5Hex(password).toLowerCase();
	}

	public static String SHA256Encode(String password) {
		return DigestUtils.sha256Hex(password.getBytes());
	}

	public static  String base64Encoder(String password){
		String base = String.valueOf(Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
		return base;
	}

	public static  String base64Decoder(String password){
		String sign = new String(Base64.getDecoder().decode(password));
		return sign;
	}

}
