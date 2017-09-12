package com.argam.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Base64Test {

	@Test
	public void base64_decoded_equal_to_original_planetext() throws IOException {
		Map<String, Object> data = Util.readJsonFromFile("data-encoded.json");
		String _data = data.get("data").toString();
		String decodedString = UtilEncode.decodeBase64(_data);
		Map<String, Object> text = Util.readJsonFromFile("data.json");
		String planeText = new ObjectMapper().writeValueAsString(text);
		System.out.println("decodedString : ");
		System.out.println(decodedString);
		System.out.println("planeText : ");
		System.out.println(planeText);
		assertEquals(planeText, decodedString);
	}

	@Test
	public void base64_encoded_equal_to_original_planetextEnconded() throws IOException {
		Map<String, Object> data = Util.readJsonFromFile("data.json");
		String encodedString = UtilEncode.encodeBase64(data);
		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("data", encodedString);
		encodedString = new ObjectMapper().writeValueAsString(wrapper);
		Map<String, Object> text = Util.readJsonFromFile("data-encoded.json");
		
		String planeTextEncoded = new ObjectMapper().writeValueAsString(text);
		System.out.println("decodedString : ");
		System.out.println(encodedString);
		System.out.println("planeText : ");
		System.out.println(planeTextEncoded);
		assertEquals(planeTextEncoded, encodedString);
	}
}
