package com.rosense.basic.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ImageUtils {
	public static final String code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private static Random random = new Random();

	public static Color getRandColor(int fc, int bc) {
		fc = Math.min(fc, 255);
		bc = Math.min(bc, 255);
		final int r = fc + random.nextInt(bc - fc);
		final int g = fc + random.nextInt(bc - fc);
		final int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static void thumbnail(final InputStream inputStream, final int width, final int height, final OutputStream outputStream)
			throws IOException {
		thumbnail(inputStream, width, height, outputStream, "png");
	}

	public static void thumbnail(final InputStream inputStream, final int width, final int height, final OutputStream outputStream,
			final String filetype) throws IOException {
		thumbnail(inputStream, width, height, false, outputStream, filetype);
	}

	public static void thumbnail(final InputStream inputStream, final int width, final int height, final boolean stretch,
			final OutputStream outputStream, final String filetype) throws IOException {
		int w, h;
		try {
			if (width == 0 && height == 0) {
				IOUtils.copyStream(inputStream, outputStream);
				return;
			}
			final BufferedImage sbi = ImageIO.read(inputStream);
			if (sbi == null) {
				return;
			}
			if (width == 0 || height == 0) {
				w = sbi.getWidth();
				h = sbi.getHeight();
			} else {
				if (!stretch) {
					final double d = (double) width / (double) height;
					final double d0 = (double) sbi.getWidth() / (double) sbi.getHeight();
					if (d < d0) {
						w = width;
						h = (int) (width / d0);
					} else {
						w = (int) (height * d0);
						h = height;
					}
				} else {
					w = width;
					h = height;
				}
			}
			final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = bi.createGraphics();
			if (w != width) {
				g.drawImage(sbi, Math.abs(w - width) / 2, 0, w, h, null);
			} else if (h != height) {
				g.drawImage(sbi, 0, Math.abs(h - height) / 2, w, h, null);
			} else {
				g.drawImage(sbi, 0, 0, w, h, null);
			}
			g.dispose();
			ImageIO.write(bi, filetype, outputStream);
		} finally {
			outputStream.close();
		}
	}

	public static boolean isImage(final URL url) throws IOException {
		final InputStream inputStream = url.openStream();
		try {
			return ImageIO.read(inputStream) != null;
		} finally {
			inputStream.close();
		}
	}

	public void cutPic(String path, String x, String x2, String y, String y2) {
		try {
			File file = new File(path);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
     * 等比压缩图像
     * 
     * @param src
     *            源图像文件
     * @param target
     *            压缩后要存放的目标文件
     * @param maxWidth
     *            压缩后允许的最大宽度
     * @param maxHeight
     *            压缩后允许的最大高度
     * @throws java.io.IOException
     */
    public static void transform(InputStream inputStream, OutputStream outputStream, int width, int height) throws Exception {
    	int w, h;
		try {
			if (width == 0 && height == 0) {
				IOUtils.copyStream(inputStream, outputStream);
				return;
			}
			final BufferedImage sbi = ImageIO.read(inputStream);
			if (sbi == null) {
				return;
			}
			if (width == 0 || height == 0) {
				w = sbi.getWidth();
				h = sbi.getHeight();
			} else {
				if (true) {
					final double d = (double) width / (double) height;
					final double d0 = (double) sbi.getWidth() / (double) sbi.getHeight();
					if (d < d0) {
						w = width;
						h = (int) (width / d0);
					} else {
						w = (int) (height * d0);
						h = height;
					}
				} else {
					w = width;
					h = height;
				}
			}
			final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = bi.createGraphics();
				g.drawImage(sbi, 0, 0, w, h, null);
			g.dispose();
			ImageIO.write(bi, "png", outputStream);
		} finally {
			outputStream.close();
		}
    }
}
