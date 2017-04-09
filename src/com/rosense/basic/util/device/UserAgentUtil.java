package com.rosense.basic.util.device;

import org.apache.commons.lang3.StringUtils;

/**
 * 根据 user agent string 判断用户的平台、浏览器 参考资料
 * ****************************************
 * **************************************
 * ********************************************************************
 * 
 * 台式机
 * 
 * Linux Ubuntu Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.2pre)
 * Gecko/20100225 Ubuntu/9.10 (karmic) Namoroka/3.6.2pre
 * ------------------------
 * ------------------------------------------------------
 * -------------------------------------------------------------------- Linux
 * Mandriva 2008.1 Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.1)
 * Gecko/2008072403 Mandriva/3.0.1-1mdv2008.1 (2008.1) Firefox/3.0.1
 * ------------
 * ------------------------------------------------------------------
 * -------------------------------------------------------------------- Linux
 * suSE 10.1 Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.3) Gecko/20060425
 * SUSE/1.5.0.3-7 Firefox/1.5.0.31
 * ----------------------------------------------
 * --------------------------------
 * -------------------------------------------------------------------- Windows
 * XP SP3 Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1)
 * Gecko/20090624 Firefox/3.5 (.NET CLR 3.5.30729)
 * ------------------------------
 * ------------------------------------------------
 * -------------------------------------------------------------------- Windows
 * Vista Mozilla/5.0 (Windows; U; Windows NT 6.1; nl; rv:1.9.2.13)
 * Gecko/20101203 Firefox/3.6.13 Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US;
 * rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6 (.NET CLR 3.5.30729)
 * ----------------
 * --------------------------------------------------------------
 * -------------------------------------------------------------------- windows
 * 2000 Mozilla/5.0 (Windows; U; Windows NT 5.0; en-GB; rv:1.8.1b2)
 * Gecko/20060821 Firefox/2.0b2
 * --------------------------------------------------
 * ----------------------------
 * -------------------------------------------------------------------- Windows
 * 7 Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1
 * --
 * ----------------------------------------------------------------------------
 * -------------------------------------------------------------------- Windows
 * Server 2008 Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5)
 * Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)
 * ----------------------------
 * --------------------------------------------------
 * -------------------------------------------------------------------- iMac OSX
 * 10.7.4 Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:13.0) Gecko/20100101
 * Firefox/13.0.1
 * ----------------------------------------------------------------
 * --------------
 * -------------------------------------------------------------------- Mac OS X
 * Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.9)
 * Gecko/20100824 Firefox/3.6.9
 * --------------------------------------------------
 * ----------------------------
 * --------------------------------------------------------------------
 * 
 * 手持设备
 * 
 * iPad Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us)
 * AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b
 * Safari/531.21.10
 * --------------------------------------------------------------
 * ----------------
 * -------------------------------------------------------------------- iPad 2
 * Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X; en-us) AppleWebKit/534.46
 * (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3
 * ----------------
 * --------------------------------------------------------------
 * -------------------------------------------------------------------- iPhone 4
 * Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us)
 * AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293
 * Safari/6531.22.7
 * --------------------------------------------------------------
 * ----------------
 * -------------------------------------------------------------------- iPhone 5
 * Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46
 * (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3
 * ----------------
 * --------------------------------------------------------------
 * -------------------------------------------------------------------- Android
 * Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91)
 * AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
 * ********
 * **********************************************************************
 * ********************************************************************
 * 
 */
public class UserAgentUtil {

	/**
	 * 用途：根据客户端 User Agent Strings 判断其浏览器、操作平台 if 判断的先后次序：
	 * 根据设备的用户使用量降序排列，这样对于大多数用户来说可以少判断几次即可拿到结果： >>操作系统:Windows > 苹果 > 安卓 > Linux
	 * > ... >>Browser:Chrome > FF > IE > ...
	 * 
	 * @param userAgentStr
	 * @return
	 */
	public static UserAgent getUserAgent(String userAgent) {
		if (StringUtils.isBlank(userAgent)) {
			return null;
		}
		if (userAgent.contains("iPhone")) {
			return judgeBrowser(userAgent, "iPhone", "", null);// 判断浏览器
		} else if (userAgent.contains("Android")) {
			return judgeBrowser(userAgent, "Android", null, null);// 判断浏览器
		} else if (userAgent.contains("Mac OS X")) {
			return judgeBrowser(userAgent, "Mac", "", null);// 判断浏览器
		} else if (userAgent.contains("Windows")) {// 主流应用靠前
			return judgeBrowser(userAgent, "Windows", "", null);// 判断浏览器
		}
		return judgeBrowser(userAgent, "其他", null, null);// 判断浏览器
	}

	/**
	 * 用途：根据客户端 User Agent Strings 判断其浏览器 if 判断的先后次序：
	 * 根据浏览器的用户使用量降序排列，这样对于大多数用户来说可以少判断几次即可拿到结果： >>Browser:Chrome > IE11 > FF >
	 * IE > ...
	 * 
	 * @param userAgent
	 *            :user agent
	 * @param platformType
	 *            :平台
	 * @param platformSeries
	 *            :系列
	 * @param platformVersion
	 *            :版本
	 * @return
	 */
	private static UserAgent judgeBrowser(String userAgent, String platformType, String platformSeries, String platformVersion) {
		if (userAgent.contains("Chrome")) {
			String temp = userAgent.substring(userAgent.indexOf("Chrome/") + 7);// 拿到User
																				// Agent
																				// String
																				// "Chrome/"
																				// 之后的字符串,结果形如"24.0.1295.0 Safari/537.15"或"24.0.1295.0"
			String chromeVersion = null;
			if (temp.indexOf(" ") < 0) {// temp形如"24.0.1295.0"
				chromeVersion = temp;
			} else {// temp形如"24.0.1295.0 Safari/537.15"
				chromeVersion = temp.substring(0, temp.indexOf(" "));
			}
			return new UserAgent("Chrome", chromeVersion, platformType, platformSeries, platformVersion);
		} else if (userAgent.contains("rv:11.0")) {
			/**
			 * IE 11 - Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0)
			 * like Gecko
			 * 判断依据:http://msdn.microsoft.com/en-us/library/ms537503(v
			 * =vs.85).aspx
			 */
			if (userAgent.contains("rv:11.0")) {// Internet Explorer 11
				return new UserAgent("Internet Explorer", "11", platformType, platformSeries, platformVersion);
			}
		} else if (userAgent.contains("Firefox")) {
			String temp = userAgent.substring(userAgent.indexOf("Firefox/") + 8);// 拿到User
																					// Agent
																					// String
																					// "Firefox/"
																					// 之后的字符串,结果形如"16.0.1 Gecko/20121011"或"16.0.1"
			String ffVersion = null;
			if (temp.indexOf(" ") < 0) {// temp形如"16.0.1"
				ffVersion = temp;
			} else {// temp形如"16.0.1 Gecko/20121011"
				ffVersion = temp.substring(0, temp.indexOf(" "));
			}
			return new UserAgent("Firefox", ffVersion, platformType, platformSeries, platformVersion);
		} else if (userAgent.contains("Safari")) {
			/**
			 * ******* 苹果 Safari 系列 *******
			 */
			String temp = userAgent.substring(userAgent.indexOf("Safari/") + 7);
			String ffVersion = null;
			if (temp.indexOf(" ") < 0) {// temp形如"16.0.1"
				ffVersion = temp;
			} else {// temp形如"16.0.1 Gecko/20121011"
				ffVersion = temp.substring(0, temp.indexOf(" "));
			}
			return new UserAgent("Safari", ffVersion, platformType, platformSeries, platformVersion);
		} else if (userAgent.contains("MSIE")) {
			if (userAgent.contains("MSIE 10.0")) {// Internet Explorer 10
				return new UserAgent("Internet Explorer", "10", platformType, platformSeries, platformVersion);
			} else if (userAgent.contains("MSIE 9.0")) {// Internet Explorer 9
				return new UserAgent("Internet Explorer", "9", platformType, platformSeries, platformVersion);
			} else if (userAgent.contains("MSIE 8.0")) {// Internet Explorer 8
				return new UserAgent("Internet Explorer", "8", platformType, platformSeries, platformVersion);
			} else if (userAgent.contains("MSIE 7.0")) {// Internet Explorer 7
				return new UserAgent("Internet Explorer", "7", platformType, platformSeries, platformVersion);
			} else if (userAgent.contains("MSIE 6.0")) {// Internet Explorer 6
				return new UserAgent("Internet Explorer", "6", platformType, platformSeries, platformVersion);
			}
		} else {// 暂时支持以上三个主流.其它浏览器,待续...
			return new UserAgent(null, null, platformType, platformSeries, platformVersion);
		}
		return null;
	}
}