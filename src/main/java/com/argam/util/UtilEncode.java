package com.argam.util;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class UtilEncode {

	public static String decodeBase64(Object object) {
		byte[] byteArray = Base64.decodeBase64(object.toString().getBytes());
		return new String(byteArray);
	}

	public static String encodeBase64(Object object) {
		byte[] encodedBytes = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(object);
			Base64 bs = new Base64();
			encodedBytes = bs.encode(jsonInString.getBytes());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encodedBytes);
	}
}
