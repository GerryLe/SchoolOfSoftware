package com.rosense.basic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月22日 上午8:28:40
 *
 */
public final class IOUtils {
	public static final byte[] NBYTE = new byte[0];

	public static final void loadProperties(final Properties props, final String filePath) {
		try {
			loadProperties(props, new FileInputStream(filePath));
		} catch (final Exception e) {
			e.printStackTrace();// for debug
		}
	}

	public static final void loadProperties(final Properties props, final InputStream inStream) {
		if (null == inStream)
			return;
		try {
			props.load(inStream);
		} catch (final Exception e) {
			e.printStackTrace();// for debug
		} finally {
			closeIO(inStream);
		}
	}

	/**
	 * ���ڹ���Ŀ¼���ļ�������
	 */
	public static final FileFilter DIR_FILTER = new FileFilter() {
		public boolean accept(final File pathname) {
			return pathname.isDirectory();
		}
	};

	public static List<File> listAllFiles(final File file, final FileFilter filter) {
		final List<File> filesList = new ArrayList<File>(8);
		if (file.isFile()) {
			filesList.add(file);
		} else {
			final File[] files = file.listFiles(filter);
			for (final File f : files) {
				filesList.addAll(listAllFiles(f, filter));
			}
		}
		return filesList;
	}

	public static void listAllFilesNoRs(final File file, final FileFilter filter) {
		if (file.isDirectory()) {
			final File[] files = file.listFiles(filter);
			for (final File f : files) {
				listAllFilesNoRs(f, filter);
			}
		}
	}


	public static final void closeIO(final Closeable... ioObjects) {
		if (null == ioObjects || ioObjects.length == 0)//
			return;
		for (final Closeable ioObject : ioObjects) {
			try {
				if (null != ioObject)
					ioObject.close();
			} catch (final Throwable e) {// ignore
			}
		}
	}

	/**
	 * ���ڽ��MapBuf���ͷ��ļ���������,<a
	 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4724038">bug
	 * 4724038</a>
	 * 
	 * @param buffer
	 *            the Buffer to release
	 */
	public static final void closeBuffer(final Buffer buffer) {
		if (null == buffer)
			return;
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					final Method cleanerMethod = buffer.getClass().getMethod("cleaner");
					if (null == cleanerMethod)
						return null;
					cleanerMethod.setAccessible(true);
					final Object cleanerObj = cleanerMethod.invoke(buffer);
					if (null == cleanerObj)
						return null;
					final Method cleanMethod = cleanerObj.getClass().getMethod("clean");
					if (null == cleanMethod)
						return null;
					cleanMethod.invoke(cleanerObj);
				} catch (final Throwable e) {
					// do nothing
				}
				return null;
			}
		});
	}

	/**
	 * ����һ�����ļ�
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void createFile(final File file) throws IOException {
		if (file.exists())
			return;
		if (file.getParentFile() != null)
			file.getParentFile().mkdirs();
		file.createNewFile();
	}

	/**
	 * �����������ж�ȡ��ݲ�д�뵽�����
	 * 
	 * @param in
	 * @param out
	 * @return �����̿������ֽ������
	 * @throws IOException
	 */
	public static int copyStream(final InputStream in, final OutputStream out) throws IOException {
		int readedCount = 0;
		final byte[] tmpBuf = new byte[1024];// 10K
		while (true) {
			final int numRead = in.read(tmpBuf);
			if (numRead == -1)
				break;
			out.write(tmpBuf, 0, numRead);
			out.flush();
			readedCount += numRead;
		}
		return readedCount;
	}

	public static int copyStream(final InputStream in, final ByteBuffer bb) throws IOException {
		int readedCount = 0;
		final byte[] tmpBuf = new byte[1024];// 10K
		while (true) {
			final int numRead = in.read(tmpBuf);
			if (numRead == -1)
				break;
			bb.put(tmpBuf, 0, numRead);
			bb.flip();
			readedCount += numRead;
		}
		return readedCount;
	}

	/**
	 * �������������inд�����ļ�file��.
	 * 
	 * @param file
	 * @param in
	 * @throws IOException
	 */
	public static void writeToFile(final File file, final InputStream in) throws IOException {
		IOUtils.createFile(file);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			IOUtils.copyStream(in, fos);
		} finally {
			IOUtils.closeIO(fos);
		}
	}

	/**
	 * �����������bд�����ļ�file��.
	 * 
	 * @param file
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeToFile(final File file, final byte[] bytes) throws IOException {
		IOUtils.createFile(file);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
		} finally {
			IOUtils.closeIO(fos);
		}
	}

	public static byte[] getStreamBytes(final InputStream in) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copyStream(in, out);
		return out.toByteArray();
	}

	public static byte[] getBytes(final Serializable o) throws IOException {
		return IOUtils.getBytes(o, true);
	}

	public static byte[] getBytes(final Serializable o, final boolean zip) throws IOException {
		if (o == null) {
			return IOUtils.NBYTE;
		}
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(o);
		if (zip) {
			final ByteArrayOutputStream ret = new ByteArrayOutputStream();
			final ZipOutputStream zos = new ZipOutputStream(ret);
			zos.putNextEntry(new ZipEntry("obj"));
			zos.write(bos.toByteArray());
			zos.close();
			return ret.toByteArray();
		}
		return bos.toByteArray();
	}

	public static Serializable unSerial(final byte[] b) throws Exception {
		return IOUtils.unSerial(b, true);
	}

	public static Serializable unSerial(final byte[] b, final boolean zip) throws Exception {
		if (b == null || b.length == 0) {
			return null;
		}
		final ByteArrayInputStream bis = new ByteArrayInputStream(b);
		if (zip) {
			ZipInputStream zis = null;
			try {
				zis = new ZipInputStream(bis);
				if (zis.getNextEntry() != null) {
					int count;
					final byte data[] = new byte[2048];
					final ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while ((count = zis.read(data, 0, 2048)) != -1) {
						bos.write(data, 0, count);
					}
					final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
					return (Serializable) ois.readObject();
				}
				return null;
			} finally {
				if (zis != null) {
					zis.close();
				}
			}
		}
		final ObjectInputStream ois = new ObjectInputStream(bis);
		return (Serializable) ois.readObject();
	}

	/**
	 * ���ٿ����ļ��ķ���.
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(final File sourceFile, final File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = (inStream = new FileInputStream(sourceFile)).getChannel();
			destination = (outStream = new FileOutputStream(destFile)).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			closeIO(source);
			closeIO(inStream);
			closeIO(destination);
			closeIO(outStream);
		}
	}

	/**
	 * ���ٿ���һ��Ŀ¼�ṹ
	 * 
	 * @param sourceDir
	 * @param destDir
	 * @throws IOException
	 */
	public static void copyDirectory(final File sourceDir, final File destDir) throws IOException {
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		final File[] children = sourceDir.listFiles();
		for (final File sourceChild : children) {
			final String name = sourceChild.getName();
			final File destChild = new File(destDir, name);
			if (sourceChild.isDirectory()) {
				copyDirectory(sourceChild, destChild);
			} else {
				copyFile(sourceChild, destChild);
			}
		}
	}

	/**
	 * ��һ���ļ������ݶ�ȡ��һ���ַ��������κ��쳣������null
	 * 
	 * @param file
	 * @return
	 */
	public static final String readFromFileAsString(final File file) {
		try {
			return new String(IOUtils.readFromFile(file));
		} catch (final Exception e) {
			return null;
		}
	}

	public static byte[] readFromFile(final File file) throws IOException {
		return IOUtils.getStreamBytes(new FileInputStream(file));
	}

	/**
	 * ��һ�������������ȡ��һ���ַ�
	 * 
	 * @param inputStream
	 * @return
	 */
	public static final String readFromStreamAsString(final InputStream inputStream) {
		try {
			return new String(IOUtils.getStreamBytes(inputStream));
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * ���ļ��ж�ȡһ���ַ��б�
	 * 
	 * @param filename
	 *            String
	 * @return String[]
	 */
	public static List<String> readListFromFile(final String filename) throws IOException {
		return readListFromFile(new File(filename));
	}

	public static List<String> readListFromFile(final InputStream stream) throws IOException {
		return readListFromFile(stream, null);
	}

	/**
	 * @param file
	 *            Ҫ�������ļ�
	 * @param split
	 *            ��ÿһ����ݽ��зָ����ַ�
	 * @return ����б�
	 * @throws IOException
	 */
	public static List<List<String>> readListFromFile(final File file, final String split) throws IOException {
		final List<String> lines = readListFromFile(file);
		final List<List<String>> datasList = new ArrayList<List<String>>();
		for (final String line : lines) {
			final List<String> dataList = new ArrayList<String>();
			dataList.addAll(Arrays.asList(line.split(split)));
			datasList.add(dataList);
		}
		return datasList;
	}

	public static List<String> readListFromFile(final InputStream stream, final String[] exceptStartwith) throws IOException {
		final List<String> result = new ArrayList<String>();
		final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(stream));
		String s;
		try {
			while ((s = bufferedreader.readLine()) != null) {
				if (exceptStartwith != null) {
					boolean contain = false;
					for (final String except : exceptStartwith) {
						if (s.startsWith(except)) {
							contain = true;
							break;
						}
					}
					if (contain)
						continue;
				}
				result.add(s);
			}
		} catch (Exception e) {
		}finally{
			closeIO(stream);
		}
		
		return result;
	}

	public static List<String> readListFromFile(final File file) throws IOException {
		return readListFromFile(new FileInputStream(file), null);
	}

	public static List<String> readListFromFile(final File file, final String[] except) throws IOException {
		return readListFromFile(new FileInputStream(file), except);
	}

	/**
	 * ��ȫɾ��һ���ļ�����Ŀ¼��
	 * 
	 * @see #delete(File)
	 * @param file
	 */
	public static final void delete(final String file) {
		delete(new File(file));
	}

	/**
	 * ��ȫɾ��һ���ļ�����Ŀ¼��
	 * 
	 * @param fileOrDir
	 */
	public static final void delete(final File fileOrDir) {
		clearDir(fileOrDir);
		fileOrDir.delete();
		fileOrDir.deleteOnExit();
	}

	/**
	 * ���Ŀ¼
	 * 
	 * @param fileOrDir
	 */
	public static final void clearDir(final File fileOrDir) {
		if (!fileOrDir.exists())
			return;
		if (fileOrDir.isFile()) {
			fileOrDir.delete();
			fileOrDir.deleteOnExit();
			return;
		}
		final File subFiles[] = fileOrDir.listFiles();
		if (null != subFiles && subFiles.length > 0) {
			for (final File subFile : subFiles)
				delete(subFile);
		}
	}

	public static String getFileNameWithoutExtendsion(final String s) {
		String ext = null;
		final int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		}
		return ext;
	}

	public static final String read(final InputStream is, final int length) throws Exception {
		final byte[] buf = new byte[length];
		is.read(buf);
		return new String(buf);
	}

	public static boolean validateExtension(final File file, final String... exten) {
		if (exten == null) {
			return true;
		}
		final String fName = file.getName();
		final String extension = fName.substring(fName.lastIndexOf("."), fName.length()).toLowerCase();
		for (final String ext : exten)
			if (ext.toLowerCase().endsWith(extension))
				return true;
		return false;
	}

	/**
	 * ����ļ��ֽڳ�����ʾ��ǩ,��:1KB,1MB,1GB,
	 * 
	 * @param fileSize
	 * @return
	 */
	public static final String getFileSizeLabel(final long fileSize) {
		double label;
		String danWei;
		final long kb = 1024;
		if (fileSize < kb)
			return fileSize + "Byte";
		final long mb = kb * 1024;
		final long gb = mb * 1024;

		if (fileSize < mb) {
			label = (double) fileSize / (double) kb;
			danWei = "KB";
		} else if (fileSize < gb) {
			label = (double) fileSize / (double) mb;
			danWei = "MB";
		} else {
			label = (double) fileSize / (double) gb;
			danWei = "GB";
		}
		return NumberFormat.getNumberInstance().format(label) + danWei;
	}

	/**
	 * @param out
	 *            �����ӡ�ӿ�
	 * @param indentLevel
	 *            ÿһ����λ��Ӧ2���ո�λ��
	 * @param args
	 *            Ҫ�������ݼ�
	 */
	public static final void println(final PrintStream out, final int indentLevel, final Object... args) {
		final int j = indentLevel * 2;
		int i = 0;
		final StringBuilder buf = new StringBuilder(32);
		do {
			if (i >= j)
				break;
			buf.append(' ');
			i++;
		} while (true);
		for (final Object obj : args)
			buf.append(obj);
		out.println(buf.toString());
	}

	/**
	 * ѹ��GZIP
	 * @throws Exception
	 */
	public static void gzip(File sourceFile, File destFile) {
		int BUFFER = 1024;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPOutputStream gos = null;
		try {
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(destFile);
			gos = new GZIPOutputStream(fos);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = fis.read(data, 0, BUFFER)) != -1) {
				gos.write(data, 0, count);
			}
			gos.finish();
			gos.flush();
			gos.close();
		} catch (Exception e) {
		} finally {
			closeIO(fis);
			closeIO(fos);
		}
	}

	/**
	 * ʹ��Java��׼GZip�ӿڽ�ѹGZip�ļ�.
	 * 
	 * @param gzipFile
	 * @return
	 * @throws IOException
	 * @throws
	 */
	public static final File unGZip(final File gzipFile) {
		try {
			return unGZip(new GZIPInputStream(new BufferedInputStream(new FileInputStream(gzipFile))),
					new File(gzipFile.getParentFile(), gzipFile.getName() + ".unGzip"));
		} catch (final IOException ioex) {
			ioex.printStackTrace();// for debug
			return null;
		}
	}

	/**
	 * ʹ��Java��׼GZip�ӿڽ�ѹGZip�ļ�.
	 * 
	 * @param gzipFile
	 * @return
	 * @throws IOException
	 * @throws
	 */
	public static final File unGZip(final InputStream gzipFile, final File rename) {
		try {
			return unGZip(new GZIPInputStream(new BufferedInputStream(gzipFile)), new File(rename.getParentFile(), rename.getName() + ".unGzip"));
		} catch (final IOException ioex) {
			ioex.printStackTrace();// for debug
			return null;
		}
	}

	/**
	 * ʹ��Java��׼GZip�ӿڽ�ѹGZip������.
	 * 
	 * @param in
	 * @param outFile
	 * @return
	 */
	public static final File unGZip(final GZIPInputStream in, final File outFile) {
		BufferedOutputStream out = null;
		try {
			final byte data[] = new byte[10240];// 1M
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			int count;
			while ((count = in.read(data)) != -1)
				out.write(data, 0, count);
			out.flush();
			return outFile;
		} catch (final IOException e) {
			e.printStackTrace();// for debug
			return null;
		} finally {
			closeIO(in);
			closeIO(out);
		}
	}

	public static File[] unTarGZip(final File gzipFile) {
		List<File> fileList = new ArrayList<File>();
		FileInputStream fis = null;
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			fis = new FileInputStream(gzipFile);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			try {
				in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			} catch (ArchiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				BufferedOutputStream bufferedOutputStream = null;
				try {
					String name = entry.getName();
					String[] names = name.split("/");
					String fileName = gzipFile.getParentFile().getAbsolutePath();
					for (int i = 0; i < names.length; i++) {
						String str = names[i];
						fileName = fileName + File.separator + str;
					}
					File file = new File(fileName);
					IOUtils.createFile(file);
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					byte[] b = new byte[10240];
					while (bufferedInputStream.read(b) != -1) {
						bufferedOutputStream.write(b);
						bufferedOutputStream.flush();
					}
					fileList.add(file);
					entry = (TarArchiveEntry) in.getNextEntry();
				} finally {
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeIO(bufferedInputStream);
			IOUtils.closeIO(in);
			IOUtils.closeIO(fis);
		}
		return fileList.toArray(new File[fileList.size()]);
	}

	public static File[] unTarGZip(final InputStream filein, final File gzipFile) {
		List<File> fileList = new ArrayList<File>();
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(filein));
			try {
				in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			} catch (ArchiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				BufferedOutputStream bufferedOutputStream = null;
				try {
					String name = entry.getName();
					String[] names = name.split("/");
					String fileName = gzipFile.getParentFile().getAbsolutePath();
					for (int i = 0; i < names.length; i++) {
						String str = names[i];
						fileName = fileName + File.separator + str;
					}
					File file = new File(fileName);
					IOUtils.createFile(file);
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					IOUtils.copyStream(bufferedInputStream, bufferedOutputStream);
					//					byte[] b = new byte[10240];
					//					while (bufferedInputStream.read(b) != -1) {
					//						bufferedOutputStream.write(b);
					//						bufferedOutputStream.flush();
					//					}
					fileList.add(file);
					entry = (TarArchiveEntry) in.getNextEntry();
				} finally {
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeIO(bufferedInputStream);
			IOUtils.closeIO(in);
		}
		return fileList.toArray(new File[fileList.size()]);
	}

	/**
	 * ��øô��̵�ʣ��ռ�
	 * 
	 * @param file
	 * @return
	 */
	public static int getFreeSpaceM(final File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		return (int) (file.getFreeSpace() / (1024 * 1024));
	}

	public static void main(String[] args) {
		File f = new File("D:\\apache-tomcat-7.0.34\\webapps\\ROOT\\gis\\images");
		f.mkdirs();
	}
}
