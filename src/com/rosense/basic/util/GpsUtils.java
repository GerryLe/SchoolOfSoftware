package com.rosense.basic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

/**
 * 获取经纬度信息
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public class GpsUtils {

	/// 给定的经度1，纬度1；经度2，纬度2. 计算2个经纬度之间的距离。
	public static double distance(double lng1, double lat1, double lng2, double lat2) {
		lat1 = (Math.PI / 180) * lat1;
		lat2 = (Math.PI / 180) * lat2;
		lng1 = (Math.PI / 180) * lng1;
		lng2 = (Math.PI / 180) * lng2;
		//地球半径  
		double R = 6371;
		//两点间距离 km，如果想要米的话，结果*1000就可以了  
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1)) * R;
		return d * 1000;
	}

	/**
	 * 查询的地址 
	 */
	public static String getAddress(double lng, double lat) {
		String url = "http://api.map.baidu.com/geocoder/v2/?ak=5N3n99MrwM0pl20tO8Xz8CiR&location=" + lat + "," + lng + "&output=json";
		String province = null;
		try {
			StringBuffer jsonString = getJsonStringByConnection(url);
			JSONObject jsonObject = JSONObject.fromObject(jsonString.toString());
			province = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("province");
		} catch (Exception e) {
		}
		return province;
	}

	protected static StringBuffer getJsonStringByConnection(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream urlStream = connection.getInputStream();
		connection.setConnectTimeout(2000);
		connection.setReadTimeout(5000);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "utf-8"));
		StringBuffer jsonString = new StringBuffer();
		String sCurrentLine = "";
		while ((sCurrentLine = bufferedReader.readLine()) != null) {
			jsonString.append(sCurrentLine);
		}
		bufferedReader.close();
		connection.disconnect();
		return jsonString;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getAddress(120.558122, 31.480727));//经度
		System.out.println(getAddress(121.461889, 31.255771));//纬度
	}
}
