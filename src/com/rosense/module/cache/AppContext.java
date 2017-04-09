package com.rosense.module.cache;

import com.rosense.basic.util.StringUtil;


/**
 * 此类主要用于在应用中解决一些应用级别的资源定义共享
 * 
 * @author QianFei.Xu qianfei.xu@rosense.cn
 * @dateTime Jan 14, 2010 1:39:58 PM
 */
public abstract class AppContext {
	private AppContext() {
	}

	/**
	 * 当前应用的根目录,此值的最后一个字符为路径分隔符'/',默认为当前运行程序的主目录("./")
	 */
	public static String appPath = System.getProperty("user.dir", ".")+'/';

	/**
	 * 此方法用于获得实际的路径,以{@link #appPath}为根目录,传入的多个数据项目将采用连接的方法组合出一个文件/目录路径<BR>
	 * 
	 * <pre>
	 * ex:getRealPath("WEB-INF/config","projects.xml") will get "${appPath}WEB-INF/config/projects.xml"<BR>
	 * ex:getRealPath("WEB-INF/config/projects.xml") will get "${appPath}WEB-INF/config/projects.xml"
	 * </pre>
	 * 
	 * @param items
	 * @return
	 */
	public static final String getRealPath(final String... items) {
		final StringBuilder buf = new StringBuilder(8);
		buf.append(appPath);
		buf.append(StringUtil.joinString(items, "/"));
		return buf.toString();
	}

	/**
	 * 此方法只是一个工具方法,用于返回应用主目录下app-config目录的对应资源路径
	 * 
	 * @param configFileName
	 * @return
	 */
	public static final String getAppConfig(final String configFileName) {
		return getRealPath("app-config", configFileName);
	}
}
