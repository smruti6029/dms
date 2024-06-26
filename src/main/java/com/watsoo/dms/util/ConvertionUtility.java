package com.watsoo.dms.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConvertionUtility {

	public static Double convertKilonotsTokm(Double value) {

		double result = value * 1.852;
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(result));

	}
	
	 public static JsonObject convertStringToJson(String textMessage) {
	        // Remove the trailing semicolon if present
	        if (textMessage.endsWith(";")) {
	            textMessage = textMessage.substring(0, textMessage.length() - 1);
	        }

	        // Split the string into key-value pairs
	        String[] pairs = textMessage.split(";");

	        // Create a map to store the key-value pairs
	        Map<String, String> map = new HashMap<>();
	        for (String pair : pairs) {
	            String[] keyValue = pair.split(":");
	            if (keyValue.length == 2) {
	                map.put(keyValue[0], keyValue[1]);
	            }
	        }

	        // Convert the map to a JSON object
	        Gson gson = new Gson();
	        JsonObject jsonObject = gson.toJsonTree(map).getAsJsonObject();

	        // Handle the Filename key as a special case
	        if (jsonObject.has("Filename")) {
	            String filenames = jsonObject.get("Filename").getAsString();
	            String[] filenameArray = filenames.split(",");
	            jsonObject.add("Filename", gson.toJsonTree(Arrays.asList(filenameArray)));
	        }

	        return jsonObject;
	    }

}
