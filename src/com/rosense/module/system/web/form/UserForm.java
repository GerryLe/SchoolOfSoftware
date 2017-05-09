package com.rosense.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.rosense.basic.model.PageHelper;

public class UserForm extends PageHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String password; //登陆密码
	private Integer status = new Integer(0); //账号状态（0：允许登录，1：禁止登录）
	private String personId; //人员账号ID
	private String diffDatetime; //相差[天、时、分、秒]
	private Date startDate; //开始日期
	private Date endDate; //结束日期
	private String oldPwd; //旧密码
	private long lastLoginTime = 0;//最后登录时间
    private String employ;
	private String area;//地区
	private String account; //登陆账号、员工编号
	private String name; //姓名
	private String workType; //上班方式
    private String age;//年龄
	private String marriage;//婚姻状况
	private String bear;//生育状况
	private String address;//现住址
	private String school;//毕业学校
	private String graduation;//毕业时间
    private String degree;//学历
	private String certificate;//证书
	private String workOld;//工作经历
	private String securityCard;//社保卡
	private String fund;//公积金簿
	private String applyRemark;//备注
	private int applyStatus;//申请状态
	private String applyDate;//申请时间
	private int approveStatus;//审批状态
	private String approveAuth;//审批权限（1：可审批，0：不可审批）
	private  String cornet;//短号
	
	//学生信息
	private String stu_no;//学号
	private String class_id;//班级号
	private String class_name;//班级名称
	private String stu_name;//姓名
	private String graduate_school;//毕业学校
	private Date entrance_date;//入学日期
	private String entrance_date_Str;//入学日期格式显示
	
	
	private String sex; //性别
	private String phone; //联系电话
	private String email; //邮箱地址
	private String remark; //备注
	private String photo; //头像路径
	private String province;//省份
	private String city;//地市
	private String grade;//类别
	private String birthday; //出生年月
	private String nation;//民族
	private String politicalFace;//政治面面貌
	private String origin;//籍贯
	private String accountAddr;//户籍地址
	private String accountPro;//户口性质
	private String idcard;//身份证号码
	private String profession;//专业
	private String contact;//联系人
	private String contactPhone;//联系人电话
	private String material;//资料
	private String bankCard;//银行卡
	private String param1;
	private String role_ids; //角色Ids
	private String role_names; //角色名称
	
	private String tea_no;//编号
	private String tea_name;//姓名
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getDiffDatetime() {
		return diffDatetime;
	}
	public void setDiffDatetime(String diffDatetime) {
		this.diffDatetime = diffDatetime;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getEmploy() {
		return employ;
	}
	public void setEmploy(String employ) {
		this.employ = employ;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getBear() {
		return bear;
	}
	public void setBear(String bear) {
		this.bear = bear;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGraduation() {
		return graduation;
	}
	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getWorkOld() {
		return workOld;
	}
	public void setWorkOld(String workOld) {
		this.workOld = workOld;
	}
	public String getSecurityCard() {
		return securityCard;
	}
	public void setSecurityCard(String securityCard) {
		this.securityCard = securityCard;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public String getApplyRemark() {
		return applyRemark;
	}
	public void setApplyRemark(String applyRemark) {
		this.applyRemark = applyRemark;
	}
	public int getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public int getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getApproveAuth() {
		return approveAuth;
	}
	public void setApproveAuth(String approveAuth) {
		this.approveAuth = approveAuth;
	}
	public String getCornet() {
		return cornet;
	}
	public void setCornet(String cornet) {
		this.cornet = cornet;
	}
	public String getStu_no() {
		return stu_no;
	}
	public void setStu_no(String stu_no) {
		this.stu_no = stu_no;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getGraduate_school() {
		return graduate_school;
	}
	public void setGraduate_school(String graduate_school) {
		this.graduate_school = graduate_school;
	}
	public Date getEntrance_date() {
		return entrance_date;
	}
	public void setEntrance_date(Date entrance_date) {
		this.entrance_date = entrance_date;
	}
	public String getEntrance_date_Str() {
		return entrance_date_Str;
	}
	public void setEntrance_date_Str(String entrance_date_Str) {
		this.entrance_date_Str = entrance_date_Str;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPoliticalFace() {
		return politicalFace;
	}
	public void setPoliticalFace(String politicalFace) {
		this.politicalFace = politicalFace;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getAccountAddr() {
		return accountAddr;
	}
	public void setAccountAddr(String accountAddr) {
		this.accountAddr = accountAddr;
	}
	public String getAccountPro() {
		return accountPro;
	}
	public void setAccountPro(String accountPro) {
		this.accountPro = accountPro;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getRole_ids() {
		return role_ids;
	}
	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}
	public String getRole_names() {
		return role_names;
	}
	public void setRole_names(String role_names) {
		this.role_names = role_names;
	}
	public String getTea_no() {
		return tea_no;
	}
	public void setTea_no(String tea_no) {
		this.tea_no = tea_no;
	}
	public String getTea_name() {
		return tea_name;
	}
	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

}
