package com.rosense.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 递增类
 * @author 黄家乐
 * 	
 * 2016年5月12日 上午10:51:49
 *
 */
@Entity
@Table(name = "rosense_increment")
public class IncrementEntity {
	protected String id;
	private int value;

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
