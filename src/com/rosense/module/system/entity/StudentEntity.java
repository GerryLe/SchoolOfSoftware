package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 学生实体
 *
 */
@Entity
@Table(name = "simple_student")
public class StudentEntity extends IdEntity {
	
	private String stu_no;//学号
	private String class_id;//班级号
	//private String class_name;//班级名称
	private String stu_name;//姓名
	private String sex; //性别
	private String phone; //联系电话
	private String email; //邮箱地址
	private String remark; //备注
	private String photo; //头像路径
	private String province;//省份
	private String city;//地市
	private Date entrance_date;//入学日期
	private String entrance_date_Str;//入学日期格式显示
	private String grade;//类别
	private String birthday; //出生年月
	private String nation;//民族
	private String politicalFace;//政治面面貌
	private String origin;//籍贯
	private String accountAddr;//户籍地址
	private String accountPro;//户口性质
	private String idcard;//身份证号码
	private String graduate_school;//毕业学校
	private String profession;//专业
	private String contact;//联系人
	private String contactPhone;//联系人电话
	private String material;//资料
	private String bankCard;//银行卡
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
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
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
	public String getGraduate_school() {
		return graduate_school;
	}
	public void setGraduate_school(String graduate_school) {
		this.graduate_school = graduate_school;
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
   
	
}
