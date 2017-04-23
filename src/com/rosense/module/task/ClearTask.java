package com.rosense.module.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rosense.module.common.web.servlet.WebContextUtil;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
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
