package com.argam.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {

	public static String castInputStreamToString(InputStream inputStream) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		return writer.toString();
	}

	public static InputStream loadFile(String filename) {
		InputStream is = Util.class.getResourceAsStream("/json/" + filename);
		return is;
	}

	public static Object castStringToJson(String jsonTxt) {

		try {
			JSONObject json = null;
			json = new JSONObject(jsonTxt);
			return jsonToMap(json);
		} catch (Exception e) {
			JSONArray json = null;
			json = new JSONArray(jsonTxt);
			return toList(json);
		}
	}

	public static Map<String, Object> readJsonFromFile(String filename) {
		InputStream is = Util.class.getResourceAsStream("/json/" + filename);
		String jsonTxt;
		JSONObject json = null;
		try {
			jsonTxt = IOUtils.toString(is);
			json = new JSONObject(jsonTxt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonToMap(json);
	}

	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			} else if (value instanceof Boolean) {
				value = new Boolean(value.toString());
			} else if (value instanceof Date) {
				value = Util.getDateFormat((java.util.Date) value);
			} else if (value.toString().contains(":")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					value = sdf.parse(value.toString());
				} catch (ParseException e) {
					System.out.println("No se puede castear la fecha a Date este Dato :" + value.toString());
				}
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public static String getDateFormat(Date date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String reportDate = df.format(date);
		return reportDate;
	}

}
