package com.rosense.module.task;

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
public class ClearTask {

	/**
	 * 每10秒处理一次
	 */
	@Scheduled(cron = "0 0 2 * * ? ")
	public void task() {
		WebContextUtil.caches.clear();
	}

}
