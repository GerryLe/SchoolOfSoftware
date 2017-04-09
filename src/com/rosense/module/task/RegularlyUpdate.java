package com.rosense.module.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rosense.module.system.service.impl.HolidaysUserService;


@Component
public class RegularlyUpdate {
	
	HolidaysUserService hus=new HolidaysUserService();
	/**
	 * 每天
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void d() {
		hus.Paidleave();
		hus.growth();
	}
	
	/**
	 * 每月
	 */
	@Scheduled(cron = "0 0 0 21 * ?")
	public void m() {
		hus.Sickleave();
	}
	
	/**
	 * 每年
	 */
	@Scheduled(cron = "0 0 0 1 3 ?")
	public void y() {
		hus.reset();
	}

}
