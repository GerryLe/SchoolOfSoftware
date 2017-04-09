package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 专业实体
 *
 */
/*@Entity
@Table(name = "simple_profession")*/
public class ProfessionEntity extends IdEntity {
	
	private String profession_no;//专业编号
	private String profession_name;//专业名称
	private String create;//年级
	private int sum_stu;//学生人数
	private int maxNum;//最大人数
	public String getProfession_no() {
		return profession_no;
	}
	public void setProfession_no(String profession_no) {
		this.profession_no = profession_no;
	}
	public String getProfession_name() {
		return profession_name;
	}
	public void setProfession_name(String profession_name) {
		this.profession_name = profession_name;
	}
	public String getCreate() {
		return create;
	}
	public void setCreate(String create) {
		this.create = create;
	}
	public int getSum_stu() {
		return sum_stu;
	}
	public void setSum_stu(int sum_stu) {
		this.sum_stu = sum_stu;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	
	
	
}
