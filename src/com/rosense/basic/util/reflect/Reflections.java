package com.rosense.basic.util.reflect;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
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
public final class Reflections {
	private final Collection<URL> pathUrls;
	private final Collection<IPathURLFilter> pathURLfilters;
	private final Collection<ITypeFilter> typeFilters;
	private ISubTypeFilter subTypeFilter;

	public Reflections() {
		typeFilters = new ArrayList<ITypeFilter>();
		pathURLfilters = new ArrayList<IPathURLFilter>();
		this.pathUrls = ClasspathHelper.getUrlsForCurrentClasspath();
	}

	public Reflections(final Collection<URL> pathUrls) {
		this.pathUrls = pathUrls;
		typeFilters = new ArrayList<ITypeFilter>();
		pathURLfilters = new ArrayList<IPathURLFilter>();
	}

	/**
	 * @param subTypeFilter
	 *            the subTypeFilter to set
	 */
	public void setSubTypeFilter(final ISubTypeFilter subTypeFilter) {
		this.subTypeFilter = subTypeFilter;
	}

	/**
	 * @return the subTypeFilter
	 */
	public ISubTypeFilter getSubTypeFilter() {
		return subTypeFilter;
	}

	public Reflections addPathURLFilter(final IPathURLFilter pathURLFilter) {
		if (null == pathURLFilter)
			return this;
		if (!this.pathURLfilters.contains(pathURLFilter))
			this.pathURLfilters.add(pathURLFilter);
		return this;
	}

	public Reflections addTypeFilter(final ITypeFilter typeFilter) {
		if (null == typeFilter)
			return this;
		if (!this.typeFilters.contains(typeFilter))
			this.typeFilters.add(typeFilter);
		return this;
	}

	/**
	 * @see {@link ReflectUtils#createSharedReflections(String...)}
	 * @see {@link ReflectUtils#setSharedReflections(Reflections)}
	 * @see {@link ReflectUtils#listSubClass(Class)}
	 * @param baseType
	 * @return
	 */
	public Collection<String> getSubTypes(final Class<?> baseType, final String... typeNames) {//
		final ThreadPool<ListSubTypes> pool = new ThreadPool<ListSubTypes>();
		for (final URL pathUrl : pathUrls) {
			if (!acceptPathUrl(pathUrl))
				continue;
			File file = null;
			try {
				file = new File(URLDecoder.decode(pathUrl.getFile(), "UTF-8"));
			} catch (final Exception e) {
				file = new File(pathUrl.getFile());
			}
			pool.execute(new ListSubTypes(baseType, file, pathUrl, typeNames));
		}
		try {
			pool.shutdown(3, TimeUnit.MINUTES);
		} catch (final InterruptedException e) {
			e.printStackTrace();//for debug
		}
		final Collection<String> result = new ArrayList<String>();
		for (final ListSubTypes task : pool.getThreadRunables()) {
			result.addAll(task.result);
		}
		return result;
	}

	class ListSubTypes implements Runnable {
		final File file;
		final Class<?> baseType;
		final URL pathUrl;
		final String[] typeNames;

		public ListSubTypes(final Class<?> baseType, final File file, final URL pathUrl, final String... typeNames) {
			this.baseType = baseType;
			this.file = file;
			this.pathUrl = pathUrl;
			this.typeNames = typeNames;
		}

		Collection<String> result = new ArrayList<String>(4);

		
		public void run() {
			if (file.isDirectory()) {
				listSubTypesFromDirectory(file, baseType, pathUrl, file, result, typeNames);
			} else
				listSubTypesFromJar(baseType, pathUrl, result, typeNames);
		}
	}

	/**
	 * @param baseType
	 * @param pathUrl
	 * @param result
	 */
	public void listSubTypesFromDirectory(final File baseDirectory, final Class<?> baseType, final URL pathUrl, final File directory,
			final Collection<String> result, final String... typeNames) {
		File[] files = directory.listFiles();
		if (null == files)
			files = new File[] {};
		String clazzPath;
		final int baseDirLen = baseDirectory.getAbsolutePath().length() + 1;
		for (final File file : files) {
			if (file.isDirectory()) {
				listSubTypesFromDirectory(baseDirectory, baseType, pathUrl, file, result, typeNames);
			} else {
				clazzPath = file.getAbsolutePath().substring(baseDirLen);
				clazzPath = clazzPath.replace('\\', '/');
				doTypesFilter(baseType, pathUrl, result, clazzPath, typeNames);
			}
		}
	}

	/**
	 * @param baseType
	 * @param pathUrl
	 * @param result
	 */
	public void listSubTypesFromJar(final Class<?> baseType, URL pathUrl, final Collection<String> result, final String... typeNames) {
		try {
			// It does not work with the filesystem: we must
			// be in the case of a package contained in a jar file.
			JarFile jarFile = null;
			try {
				if ("file".equals(pathUrl.getProtocol()))
					pathUrl = new URL("jar:" + pathUrl.toExternalForm() + "!/");
				jarFile = ((JarURLConnection) pathUrl.openConnection()).getJarFile();
			} catch (final Exception e) {
				final String filePath = pathUrl.getFile();
				// if on win platform
				if (filePath.indexOf(':') != -1) {
					if (pathUrl.getFile().charAt(0) == '/')
						jarFile = new JarFile(filePath.substring(1));
				}
				if (null == jarFile)
					jarFile = new JarFile(filePath);
			}
			final Enumeration<JarEntry> e = jarFile.entries();
			ZipEntry entry;
			while (e.hasMoreElements()) {
				entry = e.nextElement();
				doTypesFilter(baseType, pathUrl, result, entry.getName(), typeNames);
			}
		} catch (final IOException ioex) {
		}
	}

	private void doTypesFilter(final Class<?> baseType, final URL pathUrl, final Collection<String> result, final String clazzPath,
			final String... typeNames) {
		if (!clazzPath.endsWith(".class"))
			return;
		final int lastDotIdx = clazzPath.lastIndexOf('.');
		if (-1 == lastDotIdx)
			return;
		final String typeDef = clazzPath.substring(0, lastDotIdx).replace('/', '.');
		if (null != typeNames && typeNames.length > 0) {
			final int lastDot = typeDef.lastIndexOf('.');
			if (lastDot == -1)
				return;
			final String typeName = typeDef.substring(lastDot + 1);
			boolean withLiked = false;
			for (final String tmpTypeName : typeNames) {
				if (!typeName.contains(tmpTypeName))
					continue;
				withLiked = true;
				break;
			}
			if (withLiked == false)
				return;
		}
		if (this.typeFilters.isEmpty()) {
			if (null == this.subTypeFilter || this.subTypeFilter.accept(baseType, pathUrl, clazzPath))
				result.add(typeDef);
		} else {
			for (final ITypeFilter typeFilter : this.typeFilters) {
				if (!typeFilter.accept(clazzPath))
					continue;
				if (null == this.subTypeFilter || this.subTypeFilter.accept(baseType, pathUrl, clazzPath))
					result.add(typeDef);
			}
		}
	}

	/**
	 * @param pathUrl
	 * @return
	 */
	private boolean acceptPathUrl(final URL pathUrl) {
		if (this.pathURLfilters.isEmpty())
			return true;
		for (final IPathURLFilter pathURLFilter : this.pathURLfilters) {
			if (pathURLFilter.accept(pathUrl))
				return true;
		}
		return false;
	}
}
