package com.rosense.module.common.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.rosense.basic.util.LangUtils;
import com.rosense.basic.util.PinyinUtil;
import com.rosense.basic.util.StringUtil;
import com.rosense.basic.util.cons.Const;
import com.rosense.module.cache.Caches;
import com.rosense.module.cache.CachesUtil;
import com.rosense.module.system.entity.MenuEntity;
import com.rosense.module.system.entity.ProcedureEntity;
import com.rosense.module.system.service.IMenuService;
import com.rosense.module.system.service.IProcedureService;

/**
 * 
 * 李岩飞
 * 	
 * 2015年8月18日 下午8:37:15
 *
 */
public class InitService implements BeanFactoryAware {
	private String path = "";
	private String menuPath = "";
	private String cachePath = "";//缓存文件
	private String procedurePath= ""; //流程文件
	private BeanFactory factory = null;
	@Autowired
	InitAdminService initAdminService;

	/**
	 * 服务器启动加载所有定时任务(task_enable=Y)
	 */
	public void initXml() {
		try {
			Const.loadBase();
			initbase();
			initmenu();
			//DatabaseBackup();//备份数据库
			//initprocedure();
			//初始化超级管理员账号和角色授权
			this.initAdminService.addInitAdmin();
			System.out.println("系统数据初始化完成...");
			CachesUtil.loadCaches(Caches.getInstance(), Thread.currentThread().getContextClassLoader().getResourceAsStream(cachePath), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initbase() {
		try {
			// 解析init.xml文档
			Document doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
			// 得到根元素
			Element root = doc.getRootElement();
			// 得到包名
			String pkg = root.valueOf("@package");
			// 得到根元素下的entity集合
			List<Element> entities = root.selectNodes("entity");
			for (Iterator<Element> iter = entities.iterator(); iter.hasNext();) {
				Element e = iter.next();
				//exist属性标示为1则不进行初始化
				String file = e.attributeValue("file");
				if (StringUtil.isNotEmpty(file)) {//初始化文件
					initFile(e);
				} else {
					if ("0".equals(e.attributeValue("exist"))) {
						addEntity("base", e, pkg, null, null, e.attributeValue("class"), e.attributeValue("parent"), null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int menuSort = 0;

	public void initmenu() {
		try {
			// 解析init.xml文档
			Document doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(menuPath));
			// 得到根元素
			Element root = doc.getRootElement();
			// 得到包名
			String pkg = root.valueOf("@package");

			// 得到根元素下的entity集合
			List<Element> entities = root.selectNodes("entity");
			for (Iterator<Element> iter = entities.iterator(); iter.hasNext();) {
				
				Element e = iter.next();
				//exist属性标示为1则不进行初始化
				if ("0".equals(e.attributeValue("exist"))) {
					addEntity("menu", e, pkg, null, null, e.attributeValue("class"), e.attributeValue("parent"), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//private int menuProcedureSort = 0;

	public void initprocedure() {
		try {
			// 解析init.xml文档
			Document doc = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(procedurePath));
			// 得到根元素
			Element root = doc.getRootElement();
			// 得到包名
			String pkg = root.valueOf("@package");
			// 得到根元素下的entity集合
			List<Element> entities = root.selectNodes("entity");
			for (Iterator<Element> iter = entities.iterator(); iter.hasNext();) {
				Element e = iter.next();
				//exist属性标示为1则不进行初始化
				String file = e.attributeValue("file");
				if (StringUtil.isNotEmpty(file)) {//初始化文件
					initFile(e);
				} else {
					if ("0".equals(e.attributeValue("exist"))) {
						addEntity("base", e, pkg, null, null, e.attributeValue("class"), e.attributeValue("parent"), null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param e	标签元素
	 */
	private void initFile(Element e) {
		try {
			String file = e.attributeValue("file");//导入文件

			// 2. 存储对象(调用哪一个Service的哪一个方法?)
			String methodString = e.attributeValue("method");

			// 3. 调用相应的方法存储实体
			String[] mesg = methodString.split("\\.");
			String serviceName = mesg[0];
			String methodName = mesg[1];
			// 得到Service对象
			Object serviceObject = factory.getBean(serviceName);
			// 得到要调用的Servce对象上的方法的反射类
			for (Method m : serviceObject.getClass().getMethods()) {
				if (methodName.equals(m.getName())) {
					try {// 调用这个方法
						InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
						m.invoke(serviceObject, is);
					} catch (Exception e1) {
					}
				}
			}
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
	}

	/**
	 * @param e	标签元素
	 * @param pkg 包名
	 * @param parent 父对象
	 * @param methodString 调用的方法
	 * @param clazzName	类名称
	 * @param parentField 对应实体对象的父对象属性
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addEntity(String type, Element e, String pkg, Object parent, String methodString, String clazzName, String parentField, String parentName) {
		
		try {
			if ("1".equals(e.attributeValue("exist"))) {//exist属性标示为1则不进行初始化
				return;
			}
			// 处理当前Element
			// 1. 要创建一个什么样类型的对象
			// 要创建类的全包名
			String className = pkg + "." + clazzName;
			// 根据类名创建实体对象
			Object entity = Class.forName(className).newInstance();
			// 给实体对象当中的属性赋值
			Iterator iter = e.attributeIterator();
			String nodeName = "";
			boolean delete = LangUtils.toBoolean(e.attributeValue("delete"), false);
			boolean init = LangUtils.toBoolean(e.attributeValue("init"), false);
			while (iter.hasNext()) {
				Attribute attr = (Attribute) iter.next();
				String propName = attr.getName();
				if ("name".equals(propName)) {
					nodeName = attr.getValue();
				}
				// 判断除了class和call属性的其它属性赋值
				if (!"class".equals(propName) && !"method".equals(propName) && !"delete".equals(propName)) {
					String propValue = attr.getValue();
					BeanUtils.copyProperty(entity, propName, propValue);
				}
			}
			//特殊处理菜单
			if ("MenuEntity".equals(clazzName)) {
				if (parentName != null) {
					nodeName = parentName + "_" + nodeName;
				}
				BeanUtils.copyProperty(entity, "menuId", PinyinUtil.getPinYinHeadChar(nodeName));
			}
			if ("menu".equals(type)) {
				BeanUtils.copyProperty(entity, "sort", menuSort++);
			}
			if (null != parentField) {
				// 给entity父实体属性赋值
				BeanUtils.copyProperty(entity, parentField, parent);
			}

			// 2. 存储对象(调用哪一个Service的哪一个方法?)
			String method = e.attributeValue("method");
			if (method != null) {
				methodString = method;
			}

			if (methodString == null) {
				throw new RuntimeException("无法创建实体对象,调用方法未知!");
			}

			// 3. 调用相应的方法存储实体
			String[] mesg = methodString.split("\\.");
			String serviceName = mesg[0];
			String methodName = mesg[1];
			// 得到Service对象
			Object serviceObject = factory.getBean(serviceName);
			if ((init || delete) && serviceObject instanceof IMenuService) {
				MenuEntity entity2 = (MenuEntity) entity;
				((IMenuService) serviceObject).deleteByMenuId(entity2.getMenuId());
				if (delete)//初始化不需要退出方法
					return;
			}
			// 得到要调用的Servce对象上的方法的反射类
			for (Method m : serviceObject.getClass().getMethods()) {
				if (methodName.equals(m.getName())) {
					// 调用这个方法
					try {
						m.invoke(serviceObject, entity);
					} catch (Exception e1) {
					}
				}
			}

			// 4. 考虑当前Element下有没有子元素
			List<Element> subEntities = e.elements("entity");
			for (Iterator<Element> itr = subEntities.iterator(); itr.hasNext();) {
				Element subElement = itr.next();
				addEntity(type, subElement, pkg, entity, methodString, clazzName, parentField, nodeName);
				
			}
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
	}

	//初始化procedureMenu.xml文件
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addProcedureEntity(String type, Element e, String pkg, Object parent, String methodString, String clazzName, String parentField, String parentName) {
	
		System.out.println(type);
		System.out.println(pkg);
		System.out.println(methodString);
		System.out.println("className"+clazzName);
		System.out.println(parentField);
		System.out.println();
		try {
			if ("1".equals(e.attributeValue("exist"))) {//exist属性标示为1则不进行初始化
				return;
			}
			// 处理当前Element
			// 1. 要创建一个什么样类型的对象
			// 要创建类的全包名
			String className = pkg + "." + clazzName;
			// 根据类名创建实体对象
			Object entity = Class.forName(className).newInstance();
			// 给实体对象当中的属性赋值
			Iterator iter = e.attributeIterator();
			String nodeName = "";
			boolean delete = LangUtils.toBoolean(e.attributeValue("delete"), false);
			boolean init = LangUtils.toBoolean(e.attributeValue("init"), false);
			while (iter.hasNext()) {
				Attribute attr = (Attribute) iter.next();
				String propName = attr.getName();
				if ("name".equals(propName)) {
					nodeName = attr.getValue();
				}
				// 判断除了class和call属性的其它属性赋值
				if (!"class".equals(propName) && !"method".equals(propName) && !"delete".equals(propName)) {
					String propValue = attr.getValue();
					BeanUtils.copyProperty(entity, propName, propValue);
				}
			}
			//特殊处理菜单
			if ("ProcedureEntity".equals(clazzName)) {
				if (parentName != null) {
					nodeName = parentName + "_" + nodeName;
				}
				BeanUtils.copyProperty(entity, "menuId", PinyinUtil.getPinYinHeadChar(nodeName));
			}
			if ("menu".equals(type)) {
				BeanUtils.copyProperty(entity, "sort", menuProcedureSort++);
			}
			if (null != parentField) {
				// 给entity父实体属性赋值
				BeanUtils.copyProperty(entity, parentField, parent);
			}

			// 2. 存储对象(调用哪一个Service的哪一个方法?)
			String method = e.attributeValue("method");
			if (methodString == null) {
				throw new RuntimeException("无法创建实体对象,调用方法未知!");
			}

			// 3. 调用相应的方法存储实体
			String[] mesg = methodString.split("\\.");
			String serviceName = mesg[0];
			String methodName = mesg[1];
			// 得到Service对象
			Object serviceObject = factory.getBean(serviceName);
			if ((init || delete) && serviceObject instanceof IProcedureService) {
				ProcedureEntity entity2 = (ProcedureEntity) entity;
				((IProcedureService) serviceObject).deleteByMenuId(entity2.getMenuId());
				if (delete)//初始化不需要退出方法
					return;
			}
			// 得到要调用的Servce对象上的方法的反射类
			for (Method m : serviceObject.getClass().getMethods()) {
				if (methodName.equals(m.getName())) {
					// 调用这个方法
					try {
						m.invoke(serviceObject, entity);
					} catch (Exception e1) {
					}
				}
			}

			// 4. 考虑当前Element下有没有子元素
			List<Element> subEntities = e.elements("entity");
			for (Iterator<Element> itr = subEntities.iterator(); itr.hasNext();) {
				System.out.println("fanga");
				Element subElement = itr.next();
				addProcedureEntity(type, subElement, pkg, entity, methodString, clazzName, parentField, nodeName);
				
			}
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
	}*/
	
	public void setPath(String path) {
		this.path = path;
	}

	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}


	public void setProcedurePath(String procedurePath) {
		this.procedurePath = procedurePath;
	}

	public void setBeanFactory(BeanFactory factory) throws BeansException {
		this.factory = factory;
	}
	
	
	/**
	 * 备份数据库
	 */
	public void DatabaseBackup(){
		//设置每天16点进行数据库备份
		Calendar twentyOne = Calendar.getInstance();  
        twentyOne.set(Calendar.HOUR_OF_DAY, 16);  
        twentyOne.set(Calendar.MINUTE, 0);  
        twentyOne.set(Calendar.SECOND, 0);  
  
        new Timer().schedule(new TimerTask() {  
            @Override  
            public void run() {  
           	    String getpath=getClass().getResource("/").getPath().substring(1);
    	        if (exportDatabaseTool("10.200.102.23", "root", "root", getpath+"backupDatabase", "backDatabase.sql", "candao_oa")) {
    	            System.out.println("数据库备份成功！！！");
    	        } else {
    	            System.out.println("数据库备份失败！！！");
    	        }
            }  
        }, twentyOne.getTime(), 24 * 3600 * 1000); 
    	
    }
	
	 private static boolean exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) {
	        File saveFile = new File(savePath);
	        if (!saveFile.exists()) {// 如果目录不存在
	            saveFile.mkdirs();// 创建文件夹
	        }
	        if (!savePath.endsWith(File.separator)) {
	            savePath = savePath + File.separator;
	        }
	        StringBuilder stringBuilder = new StringBuilder();
	        stringBuilder.append("mysqldump").append(" --opt").append(" -h").append(hostIP);
	        stringBuilder.append(" -u").append(userName) .append(" -p").append(password).append(" --lock-all-tables=true");
	        stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ").append(databaseName);
	        try {
	            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
	            if (process.waitFor() == 0) {// 0 表示线程正常终止。
	                return true;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	
}
