package com.rosense.module.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天24点清楚数据
 * 
 * @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
@Component
public class DataClear24Task {

	/**
	 * 每日凌晨2点
	 */
	@Scheduled(cron = "0 0 2 * * *")
	public void task() {
		try {

		} catch (Exception e) {
		}
	}
}
