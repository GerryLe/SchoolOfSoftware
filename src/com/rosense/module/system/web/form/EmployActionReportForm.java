package com.rosense.module.system.web.form;

import java.io.Serializable;

import com.rosense.basic.model.PageHelper;

public class EmployActionReportForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;//英文名
	private String chinaName;//中文名
	private String locationAndDepart;// 地点、部门
	private String effectiveDate; //实施日期
	private String account;//员工编号
	private String joinDate;//入职日期
	private String innerPositionEng;//内部职位英文
	private String innerPositionChina;//内部职位中文
	private String Grade;//公司内部级别
	private String budget;//预算状况
	private String outPositionEng;//对外名片职称
	private String outPosotionChina;//对外名片职称
	private String allPayment;//薪酬总额
	private String fromPaymentBefore;//更调前税前
	private String fromPaymentAfter;//更调前税后
	private String toPaymentBefore;//更调后税前
	private String toPaymentAfter;//更调后税后
	private String payment;//基本工资
	private String welfare;//福利
	private String otherWelfare;//其他福利
	private String orgName;//变动部门
	private String positionName;//变动职级
	private String selectGrade;//选择申请类型
	private String reasons;//理由
	private String emailType;//邮箱申请形式
	private String emailGroup;//邮箱群组
	private String openSystem;//开通系统
	private String emailDuty;//邮箱处理
}
