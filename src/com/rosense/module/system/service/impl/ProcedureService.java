package com.rosense.module.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosense.basic.dao.IBaseDao;
import com.rosense.basic.model.Msg;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.dbutil.IDBUtilsHelper;
import com.rosense.module.system.entity.ProcedureEntity;
import com.rosense.module.system.service.IProcedureService;
import com.rosense.module.system.web.form.ProcedureForm;

@Service("procedureService")
@Transactional
public class ProcedureService implements IProcedureService {

	private static Logger LOG=Logger.getLogger(ProcedureService.class);
	@Inject
	private IBaseDao<ProcedureEntity> procedureDao;
	@Inject
	private IDBUtilsHelper dbUtils;
	
	private String result="";
	
	@Override
	public Msg add(ProcedureForm form) {
	   addResetSort(form.getPid());
	   ProcedureEntity entity=new ProcedureEntity();
	   BeanUtils.copyNotNullProperties(form, entity,new String[]{"remark"});
	    if(null!=form.getPid()&& !"".equals(form.getPid().trim())){
	    	entity.setMenu(this.procedureDao.load(ProcedureEntity.class, form.getPid()));
	    }
	    this.procedureDao.add(entity);
		return new Msg(true,"添加成功");
	}

	private void addResetSort(String pid) {
		if(null!=pid || "".equals(pid.trim())){
			this.procedureDao.updateByHql("update ProcedureEntity t set t.sort=t.sort+1 where t.sort>=1 and t.menu.id is null");
		}else{
			this.procedureDao.updateByHql("update ProcedureEntity t set t.sort=t.sort+1 where t.sort>=1 and t.menu.id=?",new Object[]{pid});
		}
		
	}

	@Override
	public Msg delete(ProcedureForm form) {
		ProcedureEntity entity=this.procedureDao.load(ProcedureEntity.class, form.getId());
		int oldSort=entity.getSort();
		String pid=(null !=entity.getMenu()? entity.getMenu().getId():null);
		del(entity);
		return new Msg(true,"删除成功");
	}

	private void del(ProcedureEntity entity) {
		if(entity.getMenu()!=null&& entity.getMenus().size()>0){
			for(ProcedureEntity e:entity.getMenus()){
				del(e);
			}
		}
		
	}

	@Override
	public Msg deleteByMenuId(String menuId) {
		ProcedureEntity entity=get(menuId);
		if(entity!=null){
			del(entity);
		}
		return new Msg();
	}

	@Override
	public Msg update(ProcedureForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcedureForm get(ProcedureForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcedureEntity get(String name) {
		List<ProcedureEntity> menuList=this.procedureDao.list("from ProcedureEntity where name=?",name);
		if(menuList !=null && menuList.size()>0){
			return menuList.get(0);
		}
		return null;
	}

	@Override
	public ProcedureEntity getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 将流程图信息以json对象存储
	 */
	@Override
	public String treeToJSON(String pid) {
		String sql="";
		if(StringUtil.isEmpty(pid)){
			result="";
			sql = "select t.* from simple_procedure t where t.pid is null";
		}
		else{
		 sql = "select t.* from simple_procedure t where t.pid = '"+pid+"'";
		}
		List<ProcedureForm> list = this.procedureDao.listSQL(sql, ProcedureForm.class, false);
		if(list!=null&& list.size()>0){
		for (ProcedureForm e : list) {
			/*result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getName() + "'" + ", iconCls : '" + e.getIconCls() + "'"
					+ ", pid : '" + e.getPid() + "'";
				result += ", children : ";*/
			String sqlchild = "select t.* from simple_procedure t where t.pid='" + e.getId() + "'";
			List<ProcedureForm> childlist = this.procedureDao.listSQL(sqlchild, ProcedureForm.class, false);
			result+="[";
			if(childlist!=null&&childlist.size()>0){
				for(ProcedureForm child : childlist){
					/*result += "{" + "id : '" + child.getId() + "'" + ", name : '" + child.getName() + "'";
					result += ", children : ";*/
					result += "{" + "id : '" + child.getId() + "'" + ", name : '" + child.getName() + "'" + ", iconCls : '" + child.getIconCls() + "'"
							+ ", pid : '" + child.getPid() + "'";
						result += ", children : ";
					result+="[";
				    result = childString(result,child.getId());
				    result+="]";
					result += "},";
			   }
				result=result.substring(0, result.length() - 1);
			}
			result+="]";
			//result += "},";
		
		}
		}
		//result=result.substring(0, result.length() - 1);
		return result;
		
	}
	
	/**
	 * 非根目录节点信息
	 * @param result
	 * @param pid
	 * @return
	 */
	private String childString(String result,String pid){
		 String sql = "select t.* from simple_procedure t where t.pid = '"+pid+"'";
		List<ProcedureForm> list = this.procedureDao.listSQL(sql, ProcedureForm.class, false);
		if(list!=null&& list.size()>0){
		for (ProcedureForm e : list) {
			result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getName() + "'" + ", iconCls : '" + e.getIconCls() + "'"
					+ ", href : '" + e.getHref() + "'" + ", pid : '" + e.getPid() + "'";
				result += ", children : ";
			//result += "{" + "id : '" + e.getId() + "'" + ", name : '" + e.getName() + "'";
			//result += ", children : ";
			String sqlchild = "select t.* from simple_procedure t where t.pid='" + e.getId() + "'";
			List<ProcedureForm> childlist = this.procedureDao.listSQL(sqlchild, ProcedureForm.class, false);
			result+="[";
			if(childlist!=null&&childlist.size()>0){
				for(ProcedureForm child : childlist){
					/*result += "{" + "id : '" + child.getId() + "'" + ", name : '" + child.getName() + "'";
					result += ", children : ";*/
					result += "{" + "id : '" + child.getId() + "'" + ", name : '" + child.getName() + "'" + ", iconCls : '" + child.getIconCls() + "'"
							+ ", href : '" + child.getHref() + "'" + ", pid : '" + child.getPid() + "'";
						result += ", children : ";
					result+="[";
				    result = childString(result,child.getId());
				    result+="]";
					result += "},";
			   }
				result=result.substring(0, result.length() - 1);
			}
			result+="]";
			result += "},";
		
		}
		}
		result=result.substring(0, result.length() - 1);
		return result;
	}

	private ProcedureForm recursive(ProcedureForm form) {
		List<ProcedureForm> menus=this.procedureDao.listSQL("select t.* form simple_menu t where t.pid='"+form.getId()+"' and isShow =1 order by t.sort asc",ProcedureForm.class,false);
		if(null!=menus&& menus.size()>0){
			List<ProcedureForm> child=new ArrayList<ProcedureForm>();
			for(ProcedureForm e:menus){
				ProcedureForm recursive=this.recursive(e);
				child.add(recursive);
			}
			form.setChildren(child);
		}
 		return form;
	}

	@Override
	public List<ProcedureForm> combo(ProcedureForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportTree() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Msg isShow(ProcedureForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateResetSort(int oldSort, int newSort, String id, String pid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProcedureForm> getAllMenuTree(ProcedureForm form) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ProcedureEntity entity) {
		ProcedureEntity oldEntity=get(entity.getName());
		if(oldEntity==null){
			try{
				this.procedureDao.add(entity);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			try{
				if(entity.getSort()!=0){
					BeanUtils.copyProperties(oldEntity,entity);
					this.procedureDao.update(oldEntity);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/*ProcedureEntity oldEntity=this.procedureDao.load(ProcedureEntity.class, entity.getHref());
		
		if(entity.getId()==null){
			this.procedureDao.add(entity);
		}
		else{
		   this.procedureDao.update(entity);
		}*/
		
		
	}

}
