package com.rosense.module.system.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.rosense.basic.dao.IdEntity;

/**
 * 人员实体
 *
 */
@Entity
@Table(name = "simple_person")
public class PersonEntity extends IdEntity {
	
	/**
	 * 中文名
	 */
	private String chinaname;//中文名
	
	/**
	 * 英文名
	 */
	private String name;//英文名
	
	/**
	 * 性别
	 */
	private String sex; //性别
	
	/**
	 * 住宅电话
	 */
	private String phone; //住宅电话
	
	/**
	 * 邮箱地址
	 */
	private String email; //邮箱地址
	
	/**
	 * 备注
	 */
	private String remark; //备注
	
	/**
	 * 人员头像路径
	 */
	private String photo; //人员头像路径
	
	/**
	 * 人员所属组织机构
	 */
	private String orgId; //人员所属组织机构
	
	/**
	 * 人员所属岗位
	 */
	private String positionId; //人员所属岗位
	
	/**
	 * 省份
	 */
	private String province;//省份
	
	/**
	 * 地市
	 */
	private String city;//地市
	
	/**
	 * 入职日期
	 */
	private Date employmentDate;//入职日期
	
	/**
	 * 入职日期格式显示
	 */
	private String employmentStr;//入职日期格式显示
	
	/**
	 * 类别
	 */
	private String grade;//级别
	
	/**
	 * 是否在职
	 */
	private String job; //是否在职
	
	/**
	 * 转正日期
	 */
	private String becomeStaffDate; //转正日期
	
	/**
	 * 开始计算日期
	 */
	
	private String countStartDate;  //开始计算日期
	  
	/**
	 *出生年月
	 */
	private String birthday; //出生年月
	
	/**
	 * 学历
	 */
    private String degree;  //学历
    
    /**
	 * 雇佣形式
	 */
    private String employ;  //雇佣形式
    private String area;//地区
	//private String orgId; //部门
	private String orgChildId; //部门2（team）
	//private String chinaname;//中文名
	//private String name; //英文名
	//private String positionId; //岗位ID、目前职位
	private String positionEng; //职位英文
	private String workType; //上班方式
	//private String sex; //人员性别
   // private String birthday; //出生年月
    private String age;//年龄
    private String securityDate;//社保日期
    private String fundDate;//公积金
   // private String employmentStr;//入职日期格式显示
    private String workAge; //工龄
    private String probationLimit;  //试用期限
    private String probationEnd;//试用到期
    //private String becomeStaffDate; //试用转正日期
    private String agreementLimit;//合同期限
    private String agreementStartDate;  //合同开始计算日期
	private String agreementEndDate; //合同到期
	private String agreementTimes; //合同签订次数
	private String marriage;//婚姻状况
	private String bear;//生育状况
	private String nation;//民族
	private String politicalFace;//政治面面貌
	private String origin;//籍贯
	private String accountAddr;//户籍地址
	private String accountPro;//户口性质
	private String address;//现住址
	//private String phone; //联系电话
	private String idcard;//身份证号码
	private String school;//毕业学校
	private String graduation;//毕业时间
	private String profession;//专业
   // private String degree;//学历
	private String certificate;//证书
	private String contact;//紧急联系人
	private String contactPhone;//联系人电话
	private String workOld;//工作经历
	private String material;//提交资料
	private String bankCard;//银行卡
	private String train;//培训经历
	private String securityCard;//社保卡
	private String fund;//公积金簿
    private String leaveDate;//离职日期
    
    private int orgChargeApprove;//部门主管审批(0:未通过，1：通过，2：不通过)
	private int orgHeadApprove;//直接上司审批(0:未通过，1：通过，2：不通过)
	private int hrNotice;// HR通知部门主管(0：未通知，1：已通知)
	private String applyRemark;//备注
	private String orgChargeRemark;//部门主管备注
	private String orgHeadRemark;//部门负责人备注
	private String hrRenark;//HR备注
	private int approveStatus;//审批状态
	
	private String orgChargeName;// 部门主管姓名
	private String orgHeadName;//负责人姓名
	private String orgChargeResult;//部门主管审批结果
	private String orgHeadResult;//部门负责人审批结果
	
	private String callerid;//座机分机号
	private String locateid;//座机地区
	
	public String getEmploy() {
		return employ;
	}

	public void setEmploy(String employ) {
		this.employ = employ;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getBecomeStaffDate() {
		return becomeStaffDate;
	}

	public void setBecomeStaffDate(String becomeStaffDate) {
		this.becomeStaffDate = becomeStaffDate;
	}

	public String getCountStartDate() {
		return countStartDate;
	}

	public void setCountStartDate(String countStartDate) {
		this.countStartDate = countStartDate;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Lob
	@Column(length = 16777216)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Date getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getChinaname() {
		return chinaname;
	}

	public void setChinaname(String chinaname) {
		this.chinaname = chinaname;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEmploymentStr() {
		return employmentStr;
	}

	public void setEmploymentStr(String employmentStr) {
		this.employmentStr = employmentStr;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrgChildId() {
		return orgChildId;
	}

	public void setOrgChildId(String orgChildId) {
		this.orgChildId = orgChildId;
	}

	public String getPositionEng() {
		return positionEng;
	}

	public void setPositionEng(String positionEng) {
		this.positionEng = positionEng;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSecurityDate() {
		return securityDate;
	}

	public void setSecurityDate(String securityDate) {
		this.securityDate = securityDate;
	}

	public String getFundDate() {
		return fundDate;
	}

	public void setFundDate(String fundDate) {
		this.fundDate = fundDate;
	}

	public String getWorkAge() {
		return workAge;
	}

	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}

	public String getProbationLimit() {
		return probationLimit;
	}

	public void setProbationLimit(String probationLimit) {
		this.probationLimit = probationLimit;
	}

	public String getProbationEnd() {
		return probationEnd;
	}

	public void setProbationEnd(String probationEnd) {
		this.probationEnd = probationEnd;
	}

	public String getAgreementLimit() {
		return agreementLimit;
	}

	public void setAgreementLimit(String agreementLimit) {
		this.agreementLimit = agreementLimit;
	}

	public String getAgreementStartDate() {
		return agreementStartDate;
	}

	public void setAgreementStartDate(String agreementStartDate) {
		this.agreementStartDate = agreementStartDate;
	}

	public String getAgreementEndDate() {
		return agreementEndDate;
	}

	public void setAgreementEndDate(String agreementEndDate) {
		this.agreementEndDate = agreementEndDate;
	}

	public String getAgreementTimes() {
		return agreementTimes;
	}

	public void setAgreementTimes(String agreementTimes) {
		this.agreementTimes = agreementTimes;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
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

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
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

	public String getWorkOld() {
		return workOld;
	}

	public void setWorkOld(String workOld) {
		this.workOld = workOld;
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

	public String getTrain() {
		return train;
	}

	public void setTrain(String train) {
		this.train = train;
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

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public int getOrgChargeApprove() {
		return orgChargeApprove;
	}

	public void setOrgChargeApprove(int orgChargeApprove) {
		this.orgChargeApprove = orgChargeApprove;
	}

	public int getOrgHeadApprove() {
		return orgHeadApprove;
	}

	public void setOrgHeadApprove(int orgHeadApprove) {
		this.orgHeadApprove = orgHeadApprove;
	}

	public int getHrNotice() {
		return hrNotice;
	}

	public void setHrNotice(int hrNotice) {
		this.hrNotice = hrNotice;
	}

	public String getApplyRemark() {
		return applyRemark;
	}

	public void setApplyRemark(String applyRemark) {
		this.applyRemark = applyRemark;
	}

	public String getOrgChargeRemark() {
		return orgChargeRemark;
	}

	public void setOrgChargeRemark(String orgChargeRemark) {
		this.orgChargeRemark = orgChargeRemark;
	}

	public String getOrgHeadRemark() {
		return orgHeadRemark;
	}

	public void setOrgHeadRemark(String orgHeadRemark) {
		this.orgHeadRemark = orgHeadRemark;
	}

	public String getHrRenark() {
		return hrRenark;
	}

	public void setHrRenark(String hrRenark) {
		this.hrRenark = hrRenark;
	}

	public String getOrgChargeName() {
		return orgChargeName;
	}

	public void setOrgChargeName(String orgChargeName) {
		this.orgChargeName = orgChargeName;
	}

	public String getOrgHeadName() {
		return orgHeadName;
	}

	public void setOrgHeadName(String orgHeadName) {
		this.orgHeadName = orgHeadName;
	}

	public String getOrgChargeResult() {
		return orgChargeResult;
	}

	public void setOrgChargeResult(String orgChargeResult) {
		this.orgChargeResult = orgChargeResult;
	}

	public String getOrgHeadResult() {
		return orgHeadResult;
	}

	public void setOrgHeadResult(String orgHeadResult) {
		this.orgHeadResult = orgHeadResult;
	}

	public String getCallerid() {
		return callerid;
	}

	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}

	public String getLocateid() {
		return locateid;
	}

	public void setLocateid(String locateid) {
		this.locateid = locateid;
	}

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	
}
