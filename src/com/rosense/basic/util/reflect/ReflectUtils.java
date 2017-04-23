package com.rosense.basic.util.reflect;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * 
* @author 黄家乐
 * 	
 * 2017年3月20日 
 *
 */
public final class ReflectUtils {
	public static final String VAR_START_FLAG = "${";
	public static final String VAR_END_FLAG = "}";

	private static Reflections sharedReflections;
	static final Collection<String> EMP_COLL = Collections.emptyList();

	public static final void createSharedReflections(final String... filterExts) {
		final Reflections refs = new Reflections();
		refs.addPathURLFilter(new PathURLFilter(filterExts));//
		refs.addTypeFilter(TypeFilter.DEFAULT);
		refs.setSubTypeFilter(SampleSubInstanceFilter.DEFAULT);
		ReflectUtils.setSharedReflections(refs);
	}

	/**
	 * 此方法用于绑定一个通用的共享类型遍列工具.
	 * @param sharedReflections
	 */
	public static final void setSharedReflections(final Reflections sharedReflections) {
		ReflectUtils.sharedReflections = sharedReflections;
	}

	/**
	 * 调用此方法之前必须先设置共享的类型遍列工具,参考:{@link #setSharedReflections(Reflections)},
	 * 此方法主要使更方便的遍列给定类的实现,
	 * @param baseType
	 * @return
	 */
	public static final Collection<String> listSubClass(final Class<?> baseType, final String... typeNames) {//
		if (null == sharedReflections)
			return EMP_COLL;
		//调用阶段由于可能增加新的子类实现,需要每次都重新扫描,只有在发布的产品时使用保存记录的方法以提高启动速度.
		return sharedReflections.getSubTypes(baseType, typeNames);
	}

	public static List<Class<?>> listClassOfPackage(final Class<?> cType, final String extenion) {
		final List<Class<?>> result = new ArrayList<Class<?>>();
		final List<String> cPath = ReflectUtils.listClassCanonicalNameOfPackage(cType, extenion);
		for (final String path : cPath) {
			try {
				result.add(Class.forName(path, false, Thread.currentThread().getContextClassLoader()));
			} catch (final Exception e) {
				// ignore
			}
		}
		return result;
	}

	public static List<String> listClassCanonicalNameOfPackage(final Class<?> clazz, final String extenion) {
		return ReflectUtils.listNameOfPackage(clazz, extenion, true);
	}

	public static List<String> listClassNameOfPackage(final Class<?> clazz, final String extenion) {
		return ReflectUtils.listNameOfPackage(clazz, extenion, false);
	}

	public static final <T> T invokeStaticMethod(final Class<?> cType, final String methodName, final Class<T> returnType) {
		return ReflectUtils.invokeAllMethod(cType, methodName, returnType, null);
	}

	public static final <T> T invokeMethod(final Class<?> cType, final String methodName, final Class<T> returnType) {
		try {
			return ReflectUtils.invokeAllMethod(cType, methodName, returnType, cType.newInstance());
		} catch (final Exception e) {
			return ReflectUtils.invokeAllMethod(cType, methodName, returnType, null);
		}
	}

	public static final <T> T invokeAllMethod(final Class<?> cType, final String methodName, final Class<T> returnType, final Object exeObj) {
		final Method[] methods = cType.getDeclaredMethods();
		for (final Method method : methods) {
			if (methodName.equals(method.getName()) && method.getParameterTypes().length == 0) {
				try {
					method.setAccessible(true);
					return returnType.cast(method.invoke(exeObj, (Object[]) null));
				} catch (final Exception e) {
					final StringWriter out = new StringWriter(32);
					final PrintWriter pw = new PrintWriter(out);
					pw.write(e.getLocalizedMessage());
					pw.write("\r\n");
					e.printStackTrace(pw);
					System.out.println("在执行类[" + cType.getCanonicalName() + "]的[" + methodName + "]方法时出现了一个错误:" + out.toString());
					pw.close();
				}
				break;
			}
		}

		return null;
	}

	public static List<String> listNameOfPackage(final Class<?> clazz, final String extenion, final boolean fullPkgName) {
		return ReflectUtils.listNameOfPackage(clazz.getName().replace('.', '/') + ".class", extenion, fullPkgName);
	}

	public static List<String> listNameOfPackage(final String clazzPkg, final String extenion, final boolean fullPkgName) {
		final List<String> result = new ArrayList<String>();

		final StringBuffer pkgBuf = new StringBuffer(clazzPkg);

		if (pkgBuf.charAt(0) != '/')
			pkgBuf.insert(0, '/');

		final URL urlPath = ReflectUtils.class.getResource(pkgBuf.toString());

		if (null == urlPath)
			return result;

		String checkedExtenion = extenion;
		if (!extenion.endsWith(".class"))
			checkedExtenion = extenion + ".class";

		if (pkgBuf.toString().endsWith(".class"))
			pkgBuf.delete(pkgBuf.lastIndexOf("/"), pkgBuf.length());

		pkgBuf.deleteCharAt(0);

		final StringBuffer fileUrl = new StringBuffer();
		try {
			fileUrl.append(URLDecoder.decode(urlPath.toExternalForm(), "UTF-8"));
		} catch (final UnsupportedEncodingException e1) {
			fileUrl.append(urlPath.toExternalForm());
		}

		if (fileUrl.toString().startsWith("file:")) {
			fileUrl.delete(0, 5);// delete file: flag
			if (fileUrl.indexOf(":") != -1)
				fileUrl.deleteCharAt(0);// delete flag
			final String baseDir = fileUrl.substring(0, fileUrl.lastIndexOf("classes") + 8);
			ReflectUtils
					.doListNameOfPackageInDirectory(new File(baseDir), new File(baseDir), result, pkgBuf.toString(), checkedExtenion, fullPkgName);
		} else {
			ReflectUtils.doListNameOfPackageInJar(urlPath, urlPath, result, pkgBuf.toString(), checkedExtenion, fullPkgName);
		}

		return result;
	}

	/**
	 * @param baseUrl
	 * @param urlPath
	 * @param result
	 * @param clazz
	 * @param extenion
	 * @param fullPkgName
	 */
	private static void doListNameOfPackageInJar(final URL baseUrl, final URL urlPath, final List<String> result, final String clazzPkg,
			final String extenion, final boolean fullPkgName) {
		try {
			// It does not work with the filesystem: we must
			// be in the case of a package contained in a jar file.
			final JarURLConnection conn = (JarURLConnection) urlPath.openConnection();
			final JarFile jfile = conn.getJarFile();
			final Enumeration<JarEntry> e = jfile.entries();

			ZipEntry entry;
			String entryname;

			while (e.hasMoreElements()) {
				entry = e.nextElement();
				entryname = entry.getName();

				if (entryname.startsWith(clazzPkg) && entryname.endsWith(extenion)) {
					if (fullPkgName)
						result.add(entryname.substring(0, entryname.lastIndexOf('.')).replace('/', '.'));
					else
						result.add(entryname.substring(entryname.lastIndexOf('/') + 1, entryname.lastIndexOf('.')));
				}
			}
		} catch (final IOException ioex) {
		}
	}

	/**
	 * @param directory
	 * @param fullPkgName
	 * @param extenion
	 * @param clazz
	 * @param result
	 */
	private static void doListNameOfPackageInDirectory(final File baseDirectory, final File directory, final List<String> result,
			final String clazzPkg, final String extenion, final boolean fullPkgName) {
		File[] files = directory.listFiles();
		if (null == files)
			files = new File[] {};
		String clazzPath;
		final int baseDirLen = baseDirectory.getAbsolutePath().length() + 1;
		for (final File file : files) {
			if (file.isDirectory()) {
				ReflectUtils.doListNameOfPackageInDirectory(baseDirectory, file, result, clazzPkg, extenion, fullPkgName);
			} else {
				if (!file.getName().endsWith(extenion))
					continue;

				if (fullPkgName) {
					clazzPath = file.getAbsolutePath().substring(baseDirLen);
					clazzPath = clazzPath.substring(0, clazzPath.length() - 6);
					result.add(clazzPath.replace(File.separatorChar, '.'));
				} else {
					result.add(file.getName().substring(0, file.getName().length() - 6));
				}
			}
		}
	}

	/**
	 * @param subClazz
	 * @param baseClazz
	 * @return
	 */
	public static boolean isSubClass(final Class<?> subClazz, final Class<?> baseClazz) {
		if (subClazz == baseClazz)
			return true;

		final Type[] itfcs = subClazz.getGenericInterfaces();
		if (null != itfcs) {
			for (final Type itfc : itfcs) {
				if (itfc.getClass() == Class.class)
					if (ReflectUtils.isSubClass((Class<?>) itfc, baseClazz))
						return true;
			}
		}

		final Type absType = subClazz.getGenericSuperclass();
		if (null != absType && absType.getClass() == Class.class && absType != Object.class) {
			return ReflectUtils.isSubClass((Class<?>) absType, baseClazz);
		}

		return false;
	}

	/**
	 * 过滤堆栈信息,使最终返回的信息为需要的信息
	 * 
	 * @param elements
	 * @param skips
	 * @param breaks
	 * @return
	 */
	public static final StackTraceElement[] filterStackTrace(final StackTraceElement[] elements, final String[] skips, final String[] breaks) {
		final Vector<StackTraceElement> result = new Vector<StackTraceElement>();
		String className;
		for (final StackTraceElement ele : elements) {
			className = ele.getClassName();
			if (PrivateStrings.likesIn(className, skips))
				continue;
			else if (PrivateStrings.likesIn(className, breaks))
				break;

			result.add(ele);
		}
		return result.toArray(new StackTraceElement[result.size()]);
	}

	/**
	 * 因为System.err.print不会在CMD窗口中打印信息.这样导致有些信息(如ERROR)不能被发现.
	 */
	public static final void relinkSysErrToSysOut() {
		System.setErr(System.out);
		System.err.println("*****************************************************************\r\n"
				+ "***        Relink System.err to System.out SuccessFul         ***\r\n"
				+ "*****************************************************************");
	}

	public static final String dynaSetVar(final String srcStr, final Map<String, String> varContainer) {
		int sIdx = srcStr.indexOf(ReflectUtils.VAR_START_FLAG);
		if (-1 == sIdx)
			return srcStr;

		final StringBuffer buf = new StringBuffer(srcStr);

		int eIdx;
		String key, value;
		do {
			eIdx = buf.indexOf(ReflectUtils.VAR_END_FLAG, sIdx);
			if (eIdx == -1)
				break;

			key = buf.substring(sIdx + 2, eIdx);
			value = varContainer.get(key);

			if (null == value || "".equals(value.trim()))
				value = System.getProperty(key);

			if (null == value || "".equals(value.trim()))
				value = System.getenv(key);

			if (null == value || "".equals(value.trim()))
				value = key;

			buf.delete(sIdx, eIdx + 1);
			buf.insert(sIdx, value);

			sIdx = srcStr.indexOf(ReflectUtils.VAR_START_FLAG);
		} while (sIdx != -1);

		return buf.toString();
	}

	public static boolean isPublic(final Class<?> clazz, final Member member) {
		return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(clazz.getModifiers());
	}

	public static boolean isAbstractClass(final Class<?> clazz) {
		final int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
	}

	public static boolean isFinalClass(final Class<?> clazz) {
		return Modifier.isFinal(clazz.getModifiers());
	}

	public static final <T> T initClass(final String implClass, final Class<T> tType) {
		return ReflectUtils.initClass(implClass, tType, true);
	}

	public static final <T> T initClass(final String implClass, final Class<T> tType, final boolean doInit) {
		try {
			final Object object = Class.forName(implClass, doInit, Thread.currentThread().getContextClassLoader()).newInstance();
			return tType.cast(object);
		} catch (final Throwable e) {
			return null;
		}
	}

	public static <T> T getFieldVar(final Object instance, final String varName, final Class<T> tType) {
		try {
			final Field varField = findField(instance.getClass(), varName);
			varField.setAccessible(true);
			return tType.cast(varField.get(instance));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getStaticVar(final Class<?> instanceClass, final String varName, final Class<T> tType) {
		try {
			final Field varField = instanceClass.getDeclaredField(varName);
			varField.setAccessible(true);
			return tType.cast(varField.get(null));
		} catch (final Exception e) {
		}
		return null;
	}

	public static <T> void setStaticVar(final Class<?> instanceClass, final String varName, final T varValue) {
		try {
			final Field varField = instanceClass.getDeclaredField(varName);
			varField.setAccessible(true);
			varField.set(null, varValue);
		} catch (final Exception e) {
		}
	}

	/**
	 * 从给定类中查找指定名称的属性,查找时包括父类
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static final Field findField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
		Field retValue = null;
		try {
			retValue = clazz.getDeclaredField(fieldName);
		} catch (final NoSuchFieldException e) {
			final Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				retValue = ReflectUtils.findField(superclass, fieldName);
			} else
				throw e;
		}
		return retValue;
	}

	public static final <T> void setField(final Object instance, final String name, final T value) throws Throwable {
		final Field field = ReflectUtils.findField(instance.getClass(), name);
		if (!field.isAccessible())
			field.setAccessible(true);
		if (null == value) {
			field.set(field, value);
			return;
		}
		final Class<?> fieldType = field.getType();
		final Class<?> valueT = value.getClass();
		final String fieldValue = value.toString();
		if (boolean.class == fieldType)
			field.setBoolean(instance, valueT == boolean.class ? (Boolean) value : "true".equalsIgnoreCase(fieldValue));
		else if (int.class == fieldType)
			field.setInt(instance, valueT == int.class ? (Integer) value : Integer.parseInt(fieldValue));
		else if (long.class == fieldType)
			field.setLong(instance, valueT == long.class ? (Long) value : Long.parseLong(fieldValue));
		else if (short.class == fieldType)
			field.setShort(instance, valueT == short.class ? (Short) value : Short.parseShort(fieldValue));
		else if (double.class == fieldType)
			field.setDouble(instance, valueT == double.class ? (Double) value : Double.parseDouble(fieldValue));
		else if (float.class == fieldType)
			field.setFloat(instance, valueT == float.class ? (Float) value : Float.parseFloat(fieldValue));
		else if (String.class == fieldType)
			field.set(instance, fieldValue);
		// else it is Object,do nothing
	}

	/**
	 * 在给定的实例instance中,针对所有可外部访问的属性定义,如果有声明为annoType类型的,则为其注入target值
	 * 
	 * @param <T>
	 * @param instance
	 * @param annoType
	 * @param target
	 */
	public static final <T extends Annotation> void injectPublicAnno(final Object instance, final Class<T> annoType, final Object target) {
		if (null == instance)
			return;
		final Field[] fields = instance.getClass().getFields();
		for (final Field field : fields) {
			final T annoObj = field.getAnnotation(annoType);
			if (null == annoObj)// if not founed
				continue;
			try {
				if (!field.isAccessible())
					field.setAccessible(true);
				field.set(instance, target);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> void initSubTypes(final Class<?> refBase, final Collection<String> subTypes, final Collection<T> result) {
		final Set<String> initedTypes = new HashSet<String>(subTypes.size());
		final Object[] consArgs = {};
		for (final String clazz : subTypes) {
			//
			if (initedTypes.contains(clazz))
				continue;
			initedTypes.add(clazz);
			try {
				final Class<?> cType = Class.forName(clazz);
				Method getMtd = null;
				try {
					getMtd = cType.getMethod("getInstance");
				} catch (final Throwable t) {
					//ignore
				}
				Object instance;
				if (null != getMtd)
					instance = getMtd.invoke(null);
				else {
					final Constructor<?> cons = cType.getConstructors()[0];
					cons.setAccessible(true);
					instance = cons.newInstance(consArgs);
				}
				result.add((T) instance);
			} catch (final Exception e) {
				e.printStackTrace();//for debug
			}
		}
	}
}
