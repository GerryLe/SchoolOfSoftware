package com.rosense.module.task;

import java.util.HashSet;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rosense.module.common.web.servlet.WebContextUtil;

/**
 * 
 * @author Can-Dao
 *  2015年1月9日 下午3:41:11
 * 
 */
@Component
public class ClearAddTask {

	/**
	 * 每10秒处理一次
	 */
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void task() {
		Set<String> set = new HashSet<String>(WebContextUtil.addcaches.keySet());
		long s = System.currentTimeMillis() - 60 * 1000;
		for (String id : set) {
			long t = WebContextUtil.addcaches.get(id);
			if (t < s) {
				WebContextUtil.addcaches.remove(id);
			}
		}
	}

}
