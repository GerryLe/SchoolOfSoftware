package com.rosense.module.system.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rosense.basic.dao.BaseDao;
import com.rosense.basic.dao.BeanTransformerAdapter;
import com.rosense.basic.model.DataGrid;
import com.rosense.basic.model.Msg;
import com.rosense.basic.model.Pager;
import com.rosense.basic.model.SystemContext;
import com.rosense.basic.util.BeanUtils;
import com.rosense.basic.util.StringUtil;
import com.rosense.module.system.entity.StationeriesEntity;
import com.rosense.module.system.entity.StationeryEntity;
import com.rosense.module.system.service.ILogService;
import com.rosense.module.system.service.IStationeriesService;
import com.rosense.module.system.web.form.StationeryForm;

import net.sf.json.JSONObject;

@Service
@Transactional
public class StationeriesService implements IStationeriesService{
	@Inject
	private BaseDao<StationeryEntity> stationeryDao ;
	@Inject
	private BaseDao<StationeriesEntity> stationeriesDao ;

	@Inject
	public ILogService logService;

	@Override
	public Msg addstationery(StationeryForm form) {
		try {
			if(form.getAmount()<=0)
				return new Msg(false,"数量必须为正整数");
			final StationeryEntity entity = new StationeryEntity();
			BeanUtils.copyNotNullProperties(form, entity);
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if(day >= 19){
				if(month == 12){
					month = 1 ;
					year++ ;
				}else{
					month++;
				}
			}
			entity.setSubmitDate(date);
			entity.setOrderMonth(year+"-"+month+"-20");
			stationeryDao.add(entity);
			return new Msg(true,"添加成功");
		} catch (Exception e) {
			return new Msg(false,"添加失败");

		}
	}

	@Override
	public Msg deletestationery(StationeryForm form) {
		try {
			StationeryEntity entity = stationeryDao.load(StationeryEntity.class, form.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			String ym[] = entity.getOrderMonth().split("-");
			int y = Integer.parseInt(ym[0]);
			int m = Integer.parseInt(ym[1]);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if(year>y){
				return new Msg(false,"该文具已发放");
			}else if(year==y){
				if(month>m)
					return new Msg(false,"该文具已发放");
				else if(month==m)
					if(day>=20)
						return new Msg(false,"该文具已发放");
			}
			stationeryDao.delete(StationeryEntity.class, form.getId());;
			return new Msg(true,"删除成功");
		} catch (Exception e) {
			return new Msg(false,"删除失败");
		}
	}

	@Override
	public Msg updatestationery(StationeryForm form) {
		try {
			if(form.getAmount()<=0)
				return new Msg(false,"数量必须为正整数");
			StationeryEntity entity = stationeryDao.load(StationeryEntity.class, form.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			String ym[] = entity.getOrderMonth().split("-");
			int y = Integer.parseInt(ym[0]);
			int m = Integer.parseInt(ym[1]);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if(year>y){
				return new Msg(false,"该申请已过时");
			}else if(year==y){
				if(month>m)
					return new Msg(false,"该申请已过时");
				else if(month==m)
					if(day>=19)
						return new Msg(false,"该申请已过时");
			}
			entity.setAmount(form.getAmount());
			entity.setRemark(form.getRemark());
			entity.setStationeryName(form.getStationeryName());
			entity.setStationeryType(form.getStationeryType());
			entity.setStationeryUnit(form.getStationeryUnit());
			entity.setSubmitDate(calendar.getTime());
			stationeryDao.update(entity);
			return new Msg(true,"修改成功");

		} catch (Exception e) {
			return new Msg(false,"修改失败");
		}
	}

	@Override
	public DataGrid datagridstationeries(StationeryForm form) {
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			List<StationeryForm> forms = new ArrayList<StationeryForm>();
			String sql = "select s.* from stationeries_define s where 1=1 ";
			sql = addWhere(sql, form, alias);
			if(null!=form.getSort()){
				SystemContext.setSort("s."+form.getSort());
				SystemContext.setOrder(form.getOrder());
			}else{
				SystemContext.setSort("s.stationeryName");
				SystemContext.setOrder("desc");
			}
			if(null!=form.getSearch()){
				String search = new String(form.getSearch().getBytes("iso8859-1"), "utf-8");
				sql += "and s.stationeryName like '%%"+search+"%%' ";
			}
			Pager<StationeryForm> pager = stationeryDao.findSQL(sql, alias, StationeryForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (StationeryForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;

		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public DataGrid datagridstationery(StationeryForm form) {
		try {
			Map<String, Object> alias = new HashMap<String, Object>();
			List<StationeryForm> forms = new ArrayList<StationeryForm>();
			//获取全部记录
			//String sql = "select s.* from stationery_apply s where s.userId='"+form.getUserId()+"'";
			//获取上一年至现在的申请记录
			String sql = "select s.* from stationery_apply s where s.userId='"+form.getUserId()+"' and s.submitDate > date_sub(curdate(),interval 1 year) ";
			sql = addWhere(sql, form, alias);
			if(null!=form.getSort()){
				SystemContext.setSort("s."+form.getSort());
				SystemContext.setOrder(form.getOrder());
			}else{
				SystemContext.setSort("s.submitDate");
				SystemContext.setOrder("desc");
			}
			if(null!=form.getSearch()&&!"".equals(form.getSearch())){
				String search = new String(form.getSearch().getBytes("iso8859-1"),"utf-8");
				sql += "and s."+form.getSelect()+" like '%%"+search+"%%' ";
			}
			Pager<StationeryForm> pager = stationeryDao.findSQL(sql, alias, StationeryForm.class, false);
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (StationeryForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DataGrid datagridbuy(StationeryForm form) {
		String sql ;
		List<StationeryForm> forms = new ArrayList<StationeryForm>();
		try {
			Pager<StationeryForm> pager ;
			if(null!=form.getOrgName()||null!=form.getOrderMonth()){
				pager = new Pager<StationeryForm>();
				sql = "select remark,sum(amount)*price as 'totalPrice',price,sum(amount) as 'amountAll',stationeryName,stationeryType,stationeryUnit,orderMonth from stationery_apply a where a.orgId in(select o.id from simple_org o where o.pid='"+form.getOrgId()+"' union select o.id from simple_org o where o.id='"+form.getOrgId()+"') group by stationeryName,stationeryType,stationeryUnit,orderMonth having orderMonth='"+form.getOrderMonth()+"'";
				SQLQuery sqlQuery = stationeryDao.getCurrentSession().createSQLQuery(sql);
				Integer page = SystemContext.getPage();
				Integer rows = SystemContext.getRows();
				if (page == null || page < 0)
					page = 1;
				if (rows == null || rows < 0)
					rows = 30;
				pager.setPage(page);
				pager.setRows(rows);
				sqlQuery.setFirstResult((page - 1) * rows).setMaxResults(rows);
				sqlQuery.setResultTransformer(new BeanTransformerAdapter(StationeryForm.class));
				pager.setDataRows(sqlQuery.list());
				sql = "select count(*) from ("+sql+") as c";
				BigInteger count = (BigInteger) stationeryDao.getCurrentSession().createSQLQuery(sql).uniqueResult();
				pager.setTotal(count.longValue());
			}else{
				sql = "select sa.stationeryName,sa.stationeryType,sa.stationeryUnit,sa.orderMonth,s.name as orgName,sa.price,sa.amount,u.name as 'userName',sa.remark ";
				sql += " from stationery_apply sa left join simple_user u on(u.id=sa.userId) left join simple_org s ON(sa.orgId=s.id) ";
				if(null!=form.getSort()){
					SystemContext.setSort("sa."+form.getSort());
					SystemContext.setOrder(form.getOrder());
				}else{
					SystemContext.setSort("s.name desc,sa.orderMonth");
					SystemContext.setOrder("desc");
				}
				pager = stationeryDao.findSQL(sql,StationeryForm.class, false);
			}


			if (null != pager && !pager.getDataRows().isEmpty()) {
				for (StationeryForm pf : pager.getDataRows()) {
					forms.add(pf);
				}
			}
			DataGrid dg = new DataGrid();
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Msg addstationeries(StationeryForm form) {
		try {
			final StationeriesEntity entity = new StationeriesEntity();
			BeanUtils.copyNotNullProperties(form, entity);
			stationeriesDao.add(entity);
			this.logService.add("添加文具", "文具种类：[" + entity.getStationeryName() + "]");
			return new Msg(true, "添加成功！");
		} catch (Exception e) {
			return new Msg(false,"添加失败");
		}
	}

	private int equlasVal(String param) {
		String sql = "select sd.* from stationeries_define sd where " + param;
		return this.stationeriesDao.countSQL(sql, false).intValue();
	}

	@Override
	public StationeriesEntity getstationeries(StationeryForm form) {
		return stationeriesDao.load(StationeriesEntity.class, form.getId());
	}

	@Override
	public StationeryEntity getstationery(StationeryForm form) {
		return stationeryDao.load(StationeryEntity.class, form.getId());
	}

	@Override
	public Msg deletestationeries(StationeryForm form) {
		try {
			stationeriesDao.delete(StationeriesEntity.class,form.getId());
			return new Msg(true,"删除成功");
		} catch (Exception e) {
			return new Msg(false,"删除失败");
		}
	}

	@Override
	public Msg updatestationeries(StationeryForm form) {
		try {
			final StationeriesEntity entity = new StationeriesEntity();
			BeanUtils.copyNotNullProperties(form, entity);
			stationeriesDao.update(entity);
			return new Msg(true,"修改成功");
		} catch (Exception e) {
			return new Msg(false,"修改失败");
		}
	}

	@Override
	public List<StationeriesEntity> getstationerylist() {
		try {
			String sql = "select sd.* from stationeries_define sd";
			SystemContext.setSort("sd.stationeryName");
			SystemContext.setOrder("desc");
			return stationeriesDao.listSQL(sql, StationeriesEntity.class, true);
		} catch (Exception e) {
			return null;
		}
	}

	private String addWhere(String sql, StationeryForm form, Map<String, Object> params) {
		if (StringUtil.isNotEmpty(form.getFilter())) {
			JSONObject jsonObject = JSONObject.fromObject(form.getFilter());
			for (Object key : jsonObject.keySet()) {
				if(key.equals("stationeryName")){
					form.setStationeryName(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
				if(key.equals("orderMonth")){
					form.setOrderMonth(StringUtil.getEncodePra(jsonObject.get(key).toString()));
				}
			}
		}
		if (StringUtil.isNotEmpty(form.getStationeryName())) {
			try {
				params.put("stationeryName", "%%" + URLDecoder.decode(form.getStationeryName(), "UTF-8") + "%%");
				sql += " and s.stationeryName like :stationeryName";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(form.getOrderMonth())) {
			try {
				params.put("orderMonth", "%%" + URLDecoder.decode(form.getOrderMonth(), "UTF-8") + "%%");
				sql += " and s.orderMonth like :orderMonth ";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sql;
	}

	@Override
	public List<StationeryEntity> tree() {
		try {
			String sql = "select distinct(a.orderMonth) from stationery_apply a";
			List<StationeryEntity> list = this.stationeryDao.listSQL(sql, StationeryEntity.class, false);
			List<StationeryEntity> forms = new ArrayList<StationeryEntity>();
			for (StationeryEntity e : list) {
				forms.add(e);
			}
			return forms;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Msg bulkImport(HttpServletRequest request) {
		if(request instanceof MultipartHttpServletRequest){
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile file = multipartRequest.getFile("uploadFile");// Const.uploadFieldName是表单中<input type="file"/>的			name属性
				String fileName = file.getOriginalFilename();
				if(fileName.indexOf("xlsx")>-1){
					String hql = "delete from StationeriesEntity where 1=1";
					Query query = this.stationeriesDao.getCurrentSession().createQuery(hql);
					query.executeUpdate();
					InputStream is = file.getInputStream();
					XSSFWorkbook xWorkbook = new XSSFWorkbook(is);
					XSSFSheet xSheet = xWorkbook.getSheetAt(0);
					int rows = xSheet.getPhysicalNumberOfRows();//前多少行有数据
					String data = xSheet.getRow(0).getCell(0).toString();//获取第0行0列数据
					for (int i = 1; i < rows; i++) {
						if(null==xSheet.getRow(i).getCell(0)||xSheet.getRow(i).getCell(0).toString().trim().equals(""))
							continue;
						if(null==xSheet.getRow(i).getCell(3)||xSheet.getRow(i).getCell(3).toString().trim().equals(""))
							continue;
						StationeriesEntity entity = new StationeriesEntity();
						entity.setStationeryName(xSheet.getRow(i).getCell(0).toString().trim());
						entity.setStationeryType(xSheet.getRow(i).getCell(1)==null?"":xSheet.getRow(i).getCell(1).toString().trim());
						entity.setStationeryUnit(xSheet.getRow(i).getCell(2)==null?"":xSheet.getRow(i).getCell(2).toString().trim());
						entity.setPrice(Double.parseDouble(xSheet.getRow(i).getCell(3).toString()));
						entity.setRemark(xSheet.getRow(i).getCell(4)==null?"":xSheet.getRow(i).getCell(4).toString().trim());
						this.stationeriesDao.add(entity);
					}
					return new Msg(true,"导入成功");
				}else if(fileName.indexOf("xls")>-1){
					String hql = "delete from StationeriesEntity where 1=1";
					Query query = this.stationeriesDao.getCurrentSession().createQuery(hql);
					query.executeUpdate();
					InputStream is = file.getInputStream();
					HSSFWorkbook hWorkbook = new HSSFWorkbook(is);
					HSSFSheet hSheet = hWorkbook.getSheetAt(0);
					int rows = hSheet.getPhysicalNumberOfRows();//前多少行有数据
					String data = hSheet.getRow(0).getCell(0).toString();//获取第0行0列数据
					for (int i = 1; i < rows; i++) {
						if(null==hSheet.getRow(i).getCell(0)||hSheet.getRow(i).getCell(0).toString().trim().equals(""))
							continue;
						if(null==hSheet.getRow(i).getCell(3)||hSheet.getRow(i).getCell(3).toString().trim().equals(""))
							continue;
						StationeriesEntity entity = new StationeriesEntity();
						entity.setStationeryName(hSheet.getRow(i).getCell(0).toString().trim());
						entity.setStationeryType(hSheet.getRow(i).getCell(1)==null?"":hSheet.getRow(i).getCell(1).toString().trim());
						entity.setStationeryUnit(hSheet.getRow(i).getCell(2)==null?"":hSheet.getRow(i).getCell(2).toString().trim());
						entity.setPrice(Double.parseDouble(hSheet.getRow(i).getCell(3).toString()));
						entity.setRemark(hSheet.getRow(i).getCell(4)==null?"":hSheet.getRow(i).getCell(4).toString().trim());
						this.stationeriesDao.add(entity);
					}
					return new Msg(true,"导入成功");
				}else
					return new Msg(false,"文件解析失败");
				
			} catch (IOException e) {
				return new Msg(false,"导入错误");
			}
		}
		return new Msg(false,"导入错误");
	}


}
