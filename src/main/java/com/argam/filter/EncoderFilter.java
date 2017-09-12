package com.argam.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.argam.filter.util.ResettableStreamHttpServletRequest;
import com.argam.filter.util.ResettableStreamHttpServletResponse;
import com.argam.util.Util;
import com.argam.util.UtilEncode;

public class EncoderFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(
				(HttpServletRequest) request);
		ResettableStreamHttpServletResponse wrappedResponse = new ResettableStreamHttpServletResponse(
				(HttpServletResponse) response);
		String payloadIN = IOUtils.toString(wrappedRequest.getReader());
		System.out.println("payloadIN Original ****");
		System.out.println(payloadIN);
		if (payloadIN != null && !payloadIN.equals("")) {
			Map<String, Object> mapData = (Map<String, Object>) Util.castStringToJson(payloadIN);
			String payloadDecodedIN = UtilEncode.decodeBase64(mapData.get("data"));
			System.out.println("payloadIN Decoded ----");
			System.out.println(payloadDecodedIN);
			wrappedRequest.resetInputStream(payloadDecodedIN.getBytes());
		} else {
			System.out.println("payloadIN Original [VACIO]");
		}
		try {
			chain.doFilter(wrappedRequest, wrappedResponse);
			wrappedResponse.flushBuffer();
		} finally {
			String payloadOUT = new String(wrappedResponse.getCopy());
			if (payloadOUT != null && !payloadOUT.equals("")) {
				System.out.println("payloadOUT Original ----");
				System.out.println(payloadOUT);
				Object object = Util.castStringToJson(payloadOUT);
				String payloadEncodedOUT = UtilEncode.encodeBase64(object);
				payloadEncodedOUT = wrapping(payloadEncodedOUT);
				System.out.println("payloadOUT Encoded ****");
				System.out.println(payloadEncodedOUT);
				wrappedResponse.getResponse().resetBuffer();
				wrappedResponse.getResponse().getOutputStream().write(payloadEncodedOUT.getBytes());
			} else {
				System.out.println("payloadOUT Original [VACIO]");
			}

		}
	}

	public String wrapping(String payloadEncodedOUT) throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("data", payloadEncodedOUT);
		payloadEncodedOUT = new ObjectMapper().writeValueAsString(wrapper);
		return payloadEncodedOUT;
	}

	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("call filter");
	}

}
