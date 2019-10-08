package com.whty.util.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;

/**
 * 
 * @author 王维
 * 
 */
public class ImageParseImp extends ImageParser {

	@Override
	public void setBMPBackColor(String backColor) {
		BG_COLOR = backColor;
	}

	@Override
	public byte[] imageToMonochromeBMP(Bitmap sourceBitmap) {
		return formatNonochromeBMP(sourceBitmap);
	}

	@Override
	public boolean imageToMonochromeBMP(Bitmap sourceBitmap, String path) {
		if (path == null) {
			System.out.println("转换出错:存放地址为空");
			return false;
		}
		byte[] buffer = imageToMonochromeBMP(sourceBitmap);
		if (buffer == null) {
			return false;
		}
		try {
			File targetFile = new File(path);
			OutputStream fos;
			fos = new FileOutputStream(targetFile);
			try {
				fos.write(buffer);
				fos.flush();
			} catch (FileNotFoundException e) {
				System.out.println("未找到文件");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("文件读写出错");
				e.printStackTrace();
			} finally {
				fos.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("转换出错:未找到文件");
			e1.printStackTrace();
		} catch (Exception e) {
			System.out.println("转换出错:异常");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean imageToMonochromeBMP(Bitmap sourceBitmap, File targetFile) {
		if (targetFile == null) {
			System.out.println("转换出错:存放文件为空");
			return false;
		}
		byte[] buffer = imageToMonochromeBMP(sourceBitmap);
		if (buffer == null) {
			return false;
		}
		try {
			OutputStream fos;
			fos = new FileOutputStream(targetFile);
			try {
				fos.write(buffer);
				fos.flush();
			} catch (FileNotFoundException e) {
				System.out.println("未找到文件");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("文件读写出错");
				e.printStackTrace();
			} finally {
				fos.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("转换出错:未找到文件");
			e1.printStackTrace();
		} catch (Exception e) {
			System.out.println("转换出错:异常");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将bitmap转换成bmp格式图片(单色)
	 * 
	 * @param bitmap
	 *            待转换的bitmap
	 * @param fos
	 *            转换后的bmp文件输出流
	 */
	private byte[] formatNonochromeBMP(Bitmap bitmap) {
		if (bitmap == null) {
			System.out.println("转换出错:参数为空");
			return null;
		}
		try {
			int w = bitmap.getWidth(), h = bitmap.getHeight();
			int[] pixels = new int[w * h];
			bitmap.getPixels(pixels, 0, w, 0, 0, w, h);// 取得BITMAP的所有像素点
			System.out.println("原图片像素点数:" + pixels.length);

			byte[] rgb = addMonochromeBMPData(pixels, w, h);
			byte[] header = addBMPImageHeader(62 + rgb.length);
			byte[] infos = addBMPImageInfosHeader(w, h, rgb.length);
			byte[] colortable = addBMPImageColorTable(BG_COLOR);

			byte[] buffer = new byte[14 + 40 + 8 + rgb.length];

			System.arraycopy(header, 0, buffer, 0, header.length);
			System.arraycopy(infos, 0, buffer, 14, infos.length);
			System.arraycopy(colortable, 0, buffer, 54, colortable.length);
			System.arraycopy(rgb, 0, buffer, 62, rgb.length);

			return buffer;
		} catch (Exception e) {
			System.out.println("文件处理出错");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加BMP图片文件头
	 * 
	 * @param size
	 *            文件大小(字节)
	 * @return
	 */
	private byte[] addBMPImageHeader(int size) {
		byte[] buffer = new byte[14];
		// 文件格式头
		buffer[0] = 0x42;
		buffer[1] = 0x4D;
		// 文件大小
		buffer[2] = (byte) (size >> 0);
		buffer[3] = (byte) (size >> 8);
		buffer[4] = (byte) (size >> 16);
		buffer[5] = (byte) (size >> 24);
		// reserved
		buffer[6] = 0x00;
		buffer[7] = 0x00;
		// reserved
		buffer[8] = 0x00;
		buffer[9] = 0x00;
		// 从文件头开始到实际图片数据之间的偏移量
		buffer[10] = 0x3E;
		buffer[11] = 0x00;
		buffer[12] = 0x00;
		buffer[13] = 0x00;
		return buffer;
	}

	/**
	 * 添加位图信息头
	 * 
	 * @param w
	 *            图片宽(像素点)
	 * @param h
	 *            图片高(像素点)
	 * @param size
	 *            图片数据大小
	 * @return
	 */
	private byte[] addBMPImageInfosHeader(int w, int h, int size) {

		System.out.println("图片数据大小:" + size);
		byte[] buffer = new byte[40];
		// 位图信息头需要的字节数
		buffer[0] = 0x28;
		buffer[1] = 0x00;
		buffer[2] = 0x00;
		buffer[3] = 0x00;

		// 图片宽(像素)
		buffer[4] = (byte) (w >> 0);
		buffer[5] = (byte) (w >> 8);
		buffer[6] = (byte) (w >> 16);
		buffer[7] = (byte) (w >> 24);

		// 图片高(像素)
		buffer[8] = (byte) (h >> 0);
		buffer[9] = (byte) (h >> 8);
		buffer[10] = (byte) (h >> 16);
		buffer[11] = (byte) (h >> 24);

		// 颜色平面数(当前总为1)
		buffer[12] = 0x01;
		buffer[13] = 0x00;
		// 比特数/像素(每个像素点占的比特位数,单色的时候一个像素点占一位)
		buffer[14] = 0x01;
		buffer[15] = 0x00;
		// 图像数据压缩类型
		buffer[16] = 0x00;
		buffer[17] = 0x00;
		buffer[18] = 0x00;
		buffer[19] = 0x00;
		// 图像数据大小(当压缩类型为不压缩时，可设置成0)
		buffer[20] = (byte) (size >> 0);
		buffer[21] = (byte) (size >> 8);
		buffer[22] = (byte) (size >> 16);
		buffer[23] = (byte) (size >> 24);

		// buffer[24] = (byte) 0xE0;
		// buffer[25] = 0x01;
		// buffer[24] = (byte) 0xC3;
		// buffer[25] = 0x0E;
		// 水平分辨率(像素/米,0表示缺省)
		buffer[24] = (byte) 0x00;
		buffer[25] = 0x00;
		buffer[26] = 0x00;
		buffer[27] = 0x00;

		// buffer[28] = 0x02;
		// buffer[29] = 0x03;
		// buffer[28] = (byte) 0xC3;
		// buffer[29] = 0x0E;
		// 垂直分辨率(像素/米,0表示缺省)
		buffer[28] = (byte) 0x00;
		buffer[29] = 0x00;
		buffer[30] = 0x00;
		buffer[31] = 0x00;
		// 位图实际使用的彩色表中的颜色索引数(为0的时候说明使用所有的调色板项)
		buffer[32] = 0x00;
		buffer[33] = 0x00;
		buffer[34] = 0x00;
		buffer[35] = 0x00;
		// 对图像显示有重要影响的索引数(为0的时候表示都重要)
		buffer[36] = 0x02;
		buffer[37] = 0x00;
		buffer[38] = 0x00;
		buffer[39] = 0x00;
		return buffer;
	}

	/**
	 * 添加调色板信息
	 * 
	 * @param bgColor
	 *            背景底色
	 * @return
	 */
	private byte[] addBMPImageColorTable(String bgColor) {
		byte[] buffer = new byte[8];

		if (bgColor.equalsIgnoreCase(BG_WHITE)) {
			// 颜色
			buffer[0] = 0x00;
			buffer[1] = 0x00;
			buffer[2] = 0x00;
			buffer[3] = 0x00;
			// buffer[3] = (byte) 0xFF;
			// 颜色
			buffer[4] = (byte) 0xFF;
			buffer[5] = (byte) 0xFF;
			buffer[6] = (byte) 0xFF;
			buffer[7] = 0x00;
			// buffer[7] = (byte) 0xFF;
		} else {
			buffer[0] = (byte) 0xFF;
			buffer[1] = (byte) 0xFF;
			buffer[2] = (byte) 0xFF;
			buffer[3] = 0x00;
			// buffer[3] = (byte) 0xFF;
			buffer[4] = 0x00;
			buffer[5] = 0x00;
			buffer[6] = 0x00;
			buffer[7] = 0x00;
			// buffer[7] = (byte) 0xFF;
		}
		return buffer;
	}

	/**
	 * 添加单色BMP图像数据
	 * 
	 * @param b
	 *            像素点
	 * @param w
	 *            宽（像素点数）
	 * @param h
	 *            高（像素点数）
	 * @return 转换过后的单色BMP图像区数据
	 */
	private byte[] addMonochromeBMPData(int[] b, int w, int h) {
		int len = w * h;
		int bufflen = 0;
		byte[] tmp = new byte[3];
		int index = 0, bitindex = 1;

		// 一个像素点占一位
		if (w % 8 != 0)// 将8位变成1个字节,不足补0
		{
			bufflen = (w / 8 + 1);
		} else {
			bufflen = (w / 8);
		}
		while (bufflen % 4 != 0)// BMP图像数据大小，必须是4的倍数，图像数据大小不是4的倍数时用0填充补足
		{
			bufflen = bufflen + 1;
		}
		bufflen = bufflen * h;

		byte[] buffer = new byte[bufflen];

		for (int i = len - 1; i + 1 >= w; i -= w) {
			// DIB文件格式最后一行为第一行，每行按从左到右顺序
			int end = i, start = i - w + 1;
			for (int j = start; j <= end; j++) {

				tmp[0] = (byte) (b[j] >> 0);
				tmp[1] = (byte) (b[j] >> 8);
				tmp[2] = (byte) (b[j] >> 16);

				String hex = "";
				for (int g = 0; g < tmp.length; g++) {
					String temp = Integer.toHexString(tmp[g] & 0xFF);
					if (temp.length() == 1) {
						temp = "0" + temp;
					}
					hex = hex + temp;
				}

				if (bitindex > 8) {
					index += 1;
					bitindex = 1;
				}

				if (Integer.parseInt(hex.substring(0, 2), 16)
						+ Integer.parseInt(hex.substring(2, 4), 16)
						+ Integer.parseInt(hex.substring(4), 16) >= 140 * 3) {
					buffer[index] = (byte) (buffer[index] | (0x01 << (8 - bitindex)));
				}
				bitindex++;
			}
			bitindex = 1;
			while (++index % 4 != 0) {
				buffer[index] = (byte) 0x00;
			}
		}

		return buffer;
	}

}
