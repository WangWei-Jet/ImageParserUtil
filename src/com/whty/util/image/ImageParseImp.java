package com.whty.util.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;

/**
 * 
 * @author ��ά
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
			System.out.println("ת������:��ŵ�ַΪ��");
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
				System.out.println("δ�ҵ��ļ�");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("�ļ���д����");
				e.printStackTrace();
			} finally {
				fos.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("ת������:δ�ҵ��ļ�");
			e1.printStackTrace();
		} catch (Exception e) {
			System.out.println("ת������:�쳣");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean imageToMonochromeBMP(Bitmap sourceBitmap, File targetFile) {
		if (targetFile == null) {
			System.out.println("ת������:����ļ�Ϊ��");
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
				System.out.println("δ�ҵ��ļ�");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("�ļ���д����");
				e.printStackTrace();
			} finally {
				fos.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("ת������:δ�ҵ��ļ�");
			e1.printStackTrace();
		} catch (Exception e) {
			System.out.println("ת������:�쳣");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��bitmapת����bmp��ʽͼƬ(��ɫ)
	 * 
	 * @param bitmap
	 *            ��ת����bitmap
	 * @param fos
	 *            ת�����bmp�ļ������
	 */
	private byte[] formatNonochromeBMP(Bitmap bitmap) {
		if (bitmap == null) {
			System.out.println("ת������:����Ϊ��");
			return null;
		}
		try {
			int w = bitmap.getWidth(), h = bitmap.getHeight();
			int[] pixels = new int[w * h];
			bitmap.getPixels(pixels, 0, w, 0, 0, w, h);// ȡ��BITMAP���������ص�
			System.out.println("ԭͼƬ���ص���:" + pixels.length);

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
			System.out.println("�ļ��������");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���BMPͼƬ�ļ�ͷ
	 * 
	 * @param size
	 *            �ļ���С(�ֽ�)
	 * @return
	 */
	private byte[] addBMPImageHeader(int size) {
		byte[] buffer = new byte[14];
		// �ļ���ʽͷ
		buffer[0] = 0x42;
		buffer[1] = 0x4D;
		// �ļ���С
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
		// ���ļ�ͷ��ʼ��ʵ��ͼƬ����֮���ƫ����
		buffer[10] = 0x3E;
		buffer[11] = 0x00;
		buffer[12] = 0x00;
		buffer[13] = 0x00;
		return buffer;
	}

	/**
	 * ���λͼ��Ϣͷ
	 * 
	 * @param w
	 *            ͼƬ��(���ص�)
	 * @param h
	 *            ͼƬ��(���ص�)
	 * @param size
	 *            ͼƬ���ݴ�С
	 * @return
	 */
	private byte[] addBMPImageInfosHeader(int w, int h, int size) {

		System.out.println("ͼƬ���ݴ�С:" + size);
		byte[] buffer = new byte[40];
		// λͼ��Ϣͷ��Ҫ���ֽ���
		buffer[0] = 0x28;
		buffer[1] = 0x00;
		buffer[2] = 0x00;
		buffer[3] = 0x00;

		// ͼƬ��(����)
		buffer[4] = (byte) (w >> 0);
		buffer[5] = (byte) (w >> 8);
		buffer[6] = (byte) (w >> 16);
		buffer[7] = (byte) (w >> 24);

		// ͼƬ��(����)
		buffer[8] = (byte) (h >> 0);
		buffer[9] = (byte) (h >> 8);
		buffer[10] = (byte) (h >> 16);
		buffer[11] = (byte) (h >> 24);

		// ��ɫƽ����(��ǰ��Ϊ1)
		buffer[12] = 0x01;
		buffer[13] = 0x00;
		// ������/����(ÿ�����ص�ռ�ı���λ��,��ɫ��ʱ��һ�����ص�ռһλ)
		buffer[14] = 0x01;
		buffer[15] = 0x00;
		// ͼ������ѹ������
		buffer[16] = 0x00;
		buffer[17] = 0x00;
		buffer[18] = 0x00;
		buffer[19] = 0x00;
		// ͼ�����ݴ�С(��ѹ������Ϊ��ѹ��ʱ�������ó�0)
		buffer[20] = (byte) (size >> 0);
		buffer[21] = (byte) (size >> 8);
		buffer[22] = (byte) (size >> 16);
		buffer[23] = (byte) (size >> 24);

		// buffer[24] = (byte) 0xE0;
		// buffer[25] = 0x01;
		// buffer[24] = (byte) 0xC3;
		// buffer[25] = 0x0E;
		// ˮƽ�ֱ���(����/��,0��ʾȱʡ)
		buffer[24] = (byte) 0x00;
		buffer[25] = 0x00;
		buffer[26] = 0x00;
		buffer[27] = 0x00;

		// buffer[28] = 0x02;
		// buffer[29] = 0x03;
		// buffer[28] = (byte) 0xC3;
		// buffer[29] = 0x0E;
		// ��ֱ�ֱ���(����/��,0��ʾȱʡ)
		buffer[28] = (byte) 0x00;
		buffer[29] = 0x00;
		buffer[30] = 0x00;
		buffer[31] = 0x00;
		// λͼʵ��ʹ�õĲ�ɫ���е���ɫ������(Ϊ0��ʱ��˵��ʹ�����еĵ�ɫ����)
		buffer[32] = 0x00;
		buffer[33] = 0x00;
		buffer[34] = 0x00;
		buffer[35] = 0x00;
		// ��ͼ����ʾ����ҪӰ���������(Ϊ0��ʱ���ʾ����Ҫ)
		buffer[36] = 0x02;
		buffer[37] = 0x00;
		buffer[38] = 0x00;
		buffer[39] = 0x00;
		return buffer;
	}

	/**
	 * ��ӵ�ɫ����Ϣ
	 * 
	 * @param bgColor
	 *            ������ɫ
	 * @return
	 */
	private byte[] addBMPImageColorTable(String bgColor) {
		byte[] buffer = new byte[8];

		if (bgColor.equalsIgnoreCase(BG_WHITE)) {
			// ��ɫ
			buffer[0] = 0x00;
			buffer[1] = 0x00;
			buffer[2] = 0x00;
			buffer[3] = 0x00;
			// buffer[3] = (byte) 0xFF;
			// ��ɫ
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
	 * ��ӵ�ɫBMPͼ������
	 * 
	 * @param b
	 *            ���ص�
	 * @param w
	 *            �����ص�����
	 * @param h
	 *            �ߣ����ص�����
	 * @return ת������ĵ�ɫBMPͼ��������
	 */
	private byte[] addMonochromeBMPData(int[] b, int w, int h) {
		int len = w * h;
		int bufflen = 0;
		byte[] tmp = new byte[3];
		int index = 0, bitindex = 1;

		// һ�����ص�ռһλ
		if (w % 8 != 0)// ��8λ���1���ֽ�,���㲹0
		{
			bufflen = (w / 8 + 1);
		} else {
			bufflen = (w / 8);
		}
		while (bufflen % 4 != 0)// BMPͼ�����ݴ�С��������4�ı�����ͼ�����ݴ�С����4�ı���ʱ��0��䲹��
		{
			bufflen = bufflen + 1;
		}
		bufflen = bufflen * h;

		byte[] buffer = new byte[bufflen];

		for (int i = len - 1; i + 1 >= w; i -= w) {
			// DIB�ļ���ʽ���һ��Ϊ��һ�У�ÿ�а�������˳��
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
