package com.example.media.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.http.MediaType;

public class CommonUtils {
	
	private static final long jpeg = 0xffd8000000000000L, png = 0x89504E470D0A1A0AL, gif = 0x4749460000000000L;
	
	/**
	 * Get file type from binary data
	 * @param binary binary data
	 * @return MediaType
	 */
	public static MediaType getMediaType(byte[] binary) {
		long l;
		if (binary == null)
			return null;
		if (binary.length < 8)
			return null;
		l = (long) binary[0] << 56 | ((long) binary[1] & 0xffL) << 48 | ((long) binary[2] & 0xffL) << 40 | ((long) binary[3] & 0xffL) << 32 | ((long) binary[4] & 0xffL) << 24
				| ((long) binary[5] & 0xffL) << 16 | ((long) binary[6] & 0xffL) << 8 | ((long) binary[7] & 0xffL);
		if ((l & jpeg) == jpeg)
			return MediaType.IMAGE_JPEG;
		else if ((l & png) == png)
			return MediaType.IMAGE_PNG;
		else if ((l & gif) == gif)
			return MediaType.IMAGE_GIF;
		return null;
	}

	private static int TOKEN_LENGTH = 16;
	
	public static String createToken() {
	    byte token[] = new byte[TOKEN_LENGTH];
	    StringBuffer buf = new StringBuffer();
	    SecureRandom random = null;
	 
	    try {
	      random = SecureRandom.getInstance("SHA1PRNG");
	      random.nextBytes(token);
	      for (int i = 0; i < token.length; i++) {
	        buf.append(String.format("%02x", token[i]));
	      }
	    } catch (NoSuchAlgorithmException e) {
	      e.printStackTrace();
	    }	 
	    return buf.toString();	
	}

}
