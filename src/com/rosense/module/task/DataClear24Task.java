package com.rosense.module.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天24点清楚数据
 * 
 * @author Can-Dao
 *  2015年1月9日 下午3:41:11
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
