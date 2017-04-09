package com.rosense.module.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.exception.ServiceException;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.common.web.servlet.WebContextUtil;
import com.rosense.module.system.entity.EmployEntity;
import com.rosense.module.system.entity.RoleEntity;
import com.rosense.module.system.entity.UserEntity;
import com.rosense.module.system.service.IEmployService;
import com.rosense.module.system.web.form.EmployForm;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;


@Service("employService")
@Transactional
public class EmployService implements IEmployService{
	private Logger logger=Logger.getLogger(RoleService.class);
	@Inject
	private IBaseDao<EmployEntity> employDao;
	@Inject
	private IBaseDao<RoleEntity> rDao;
	@Inject
	private IBaseDao<UserEntity> userDao;
	
	
	@Override
	public Msg add(EmployForm form) {
		// TODO Auto-generated method stub
		try{
		final EmployEntity e=new EmployEntity();
		BeanUtils.copyNotNullProperties(form, e);
		employDao.add(e);
		return new Msg(true,"招聘申请成功");
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"招聘申请失败");
		}
	}
	@Override
	public Msg update(EmployForm form) {
		// TODO Auto-generated method stub
		try{
			EmployEntity e=this.employDao.load(EmployEntity.class,form.getId());
			BeanUtils.copyNotNullProperties(form, e);
			this.employDao.update(e);
			return new Msg(true,"更新成功");
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"更新失败");
		}
	}
	
	@Override
	public Msg delete(EmployForm form) {
		// TODO Auto-generated method stub
		try{
		String[] ids=form.getIds().split(",");
		for(String id:ids){
			EmployEntity e=this.employDao.load(EmployEntity.class,id);
			RoleEntity r=this.rDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			if("ADMIN".equals(r.getSn()) && 0== (r.getDefaultRole())){
				e.setApplyStatement(3);
				this.employDao.update(e);
				return new Msg(true,"总裁撤销成功");
			}
			if("CHARGE".equals(r.getSn()) && 2==(r.getDefaultRole())){
				e.setApplyStatement(3);
				return new Msg(true,"部门主管撤销成功");
			}
			if("superior".equals(r.getSn()) && 4==(r.getDefaultRole())){
				e.setApplyStatement(3);
				this.employDao.update(e);
				return new Msg(true,"直接上司撤销成功");
			}
			
			if("manager".equals(r.getSn()) && 5==(r.getDefaultRole())){
				e.setApplyStatement(3);
				this.employDao.update(e);
				return new Msg(true,"总经理撤销成功");
			}
			if(e.getApplyName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
				e.setApplyStatement(3);
				this.employDao.update(e);
				return new Msg(true,"撤销成功");
			}
			else{
				return new Msg(false,"无权撤销");
			}
		}
		return new Msg(true,"撤销成功");
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"撤销失败");
		}
	}
	@Override
	public DataGrid select(EmployForm form) {
		// TODO Auto-generated method stub
		try{
		List<EmployForm> forms=new ArrayList<EmployForm>();
		Map<String, Object> alias=new HashMap<String,Object>();
		RoleEntity r=this.rDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
		String	sql="select e.* from simple_employ e where 1=1 ";
		sql = addWhere(sql, form, alias);
		Pager<EmployForm> pager=this.employDao.findSQL(sql, alias ,EmployForm.class,false);
		if(null!=pager&& !pager.getDataRows().isEmpty()){
			for(EmployForm pf:pager.getDataRows()){
				if("ADMIN".equals(r.getSn()) && 0==r.getDefaultRole() || "CHARGE".equals(r.getSn()) && 2==(r.getDefaultRole()) ||
				   "superior".equals(r.getSn()) && 4==(r.getDefaultRole()) || "manager".equals(r.getSn()) && 5==(r.getDefaultRole())){
					forms.add(pf);
				}else{
					if(pf.getApplyName().equals(WebContextUtil.getCurrentUser().getUser().getName())){
					forms.add(pf);
					}
				}
				
			}
		}
		DataGrid pd=new DataGrid();
		pd.setTotal(pager.getTotal());
		pd.setRows(forms);
		return pd;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("加载列表信息异常",e);
		}
	}
	
	private String addWhere(String sql, EmployForm form, Map<String, Object> alias) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotEmpty(form.getFilter())){
			JSONObject jsonObject=JSONObject.fromObject(form.getFilter());
			for(Object key:jsonObject.keySet()){
				if(key.equals("applyName")){
					form.setApplyName(jsonObject.get(key).toString());
				}
			}
		}
		if(StringUtil.isNotEmpty(form.getApplyName())){
			try{
			   sql=" and applyName like : applyName";
			}catch(Exception e){
				e.printStackTrace();
				
			}
		}
		return sql;
	}
	@Override
	public EmployForm get(String id) {
		// TODO Auto-generated method stub
		try{
			String sql="select e.* from simple_employ e where e.id=?";
			EmployForm form=(EmployForm) this.employDao.queryObjectSQL(sql, new Object[]{id},EmployForm.class,false);
			RoleEntity r=this.rDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
			form.setUserRoleSn(r.getSn());
			form.setUserDefaultRole(r.getDefaultRole());
			form.setUserName(WebContextUtil.getCurrentUser().getUser().getName());
			return form;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("加载信息失败:",e);
		}
	}
	@Override
	public Msg approvaltrue(String id) {
		// TODO Auto-generated method stub
		try{
		EmployEntity e=this.employDao.load(EmployEntity.class,id);
		RoleEntity r=this.rDao.load(RoleEntity.class, WebContextUtil.getCurrentUser().getUser().getRole_ids());
		if("ADMIN".equals(r.getSn()) && 0==(r.getDefaultRole())){
		    e.setBossApply(1);
		    e.setOrgChargeApply(1);
		    e.setSuperiorApply(1);
		    e.setManagerApply(1);
		    if(e.getApplyStatement()==0){
		      e.setApplyStatement(1);
		    }
		    this.employDao.update(e);
		    return new Msg(true,"总裁审批成功");
		}
		if("CHARGE".equals(r.getSn()) && 2==(r.getDefaultRole())){
			e.setOrgChargeApply(1);
			this.employDao.update(e);
			return new Msg(true,"部门审批成功");
		}
		if("superior".equals(r.getSn()) && 4==(r.getDefaultRole())){
			e.setSuperiorApply(1);
			this.employDao.update(e);
			return new Msg(true,"总监审批成功");
		}
	    if("manager".equals(r.getSn()) && 5==(r.getDefaultRole())){
	    	e.setManagerApply(1);
	    	this.employDao.update(e);
	    	return new Msg(true,"总经理审批成功");
	    }else{
	    	return new Msg(true,"没有审批权限");
	    }
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"审批失败");
		}
		
	}
	@Override
	public Msg approvalfalse(String id) {
		// TODO Auto-generated method stub
		try{
			EmployEntity e=this.employDao.load(EmployEntity.class, id);
			RoleEntity r=this.rDao.load(RoleEntity.class,WebContextUtil.getCurrentUser().getUser().getRole_ids());
			if("ADMIN".equals(r.getSn()) && 0==(r.getDefaultRole())){
			    e.setBossApply(2);
			    e.setApplyStatement(2);
			    this.employDao.update(e);
			    return new Msg(true,"总裁已经审批");
			}
			if("CHARGE".equals(r.getSn()) && 2==(r.getDefaultRole())){
				e.setOrgChargeApply(2);
				this.employDao.update(e);
				return new Msg(true,"部门已经审批");
			}
			if("superior".equals(r.getSn()) && 4==(r.getDefaultRole())){
				e.setSuperiorApply(2);
				this.employDao.update(e);
				return new Msg(true,"总监已经审批");
			}
		    if("manager".equals(r.getSn()) && 5==(r.getDefaultRole())){
		    	e.setManagerApply(2);
		    	this.employDao.update(e);
		    	return new Msg(true,"总经理审批成功");
		    }else{
		    	return new Msg(true,"没有审批权限");
		    }
		}catch(Exception e){
			e.printStackTrace();
			return new Msg(false,"审批失败");
		}
	}
	
	
	
}
