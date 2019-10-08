package com.whty.util.image;

import java.io.File;

import android.graphics.Bitmap;

/**
 * 
 * @author ��ά
 * 
 */
public abstract class ImageParser {
	public String BG_BLACK = "background_black";
	public String BG_WHITE = "background_white";
	public String BG_COLOR = BG_BLACK;

	public abstract void setBMPBackColor(String backColor);

	/**
	 * ͼƬת��ɫBMP
	 * 
	 * @param sourceBitmap
	 *            ԭͼƬ��bitmap(��ǰ֧��PNG,JPGת��ɫBMP)
	 * @return ת���ɹ���ĵ�ɫBMP
	 */
	public abstract byte[] imageToMonochromeBMP(Bitmap sourceBitmap);

	/**
	 * ͼƬת��ɫBMP
	 * 
	 * @param sourceBitmap
	 *            ԭͼƬ��bitmap(��ǰ֧��PNG,JPGת��ɫBMP)
	 * @param path
	 *            ת���ɹ���ͼƬ�Ĵ��λ��
	 * @return ת�������Ƿ�ɹ�
	 */
	public abstract boolean imageToMonochromeBMP(Bitmap sourceBitmap,
			String path);

	/**
	 * ͼƬת��ɫBMP
	 * 
	 * @param sourceBitmap
	 *            ԭͼƬ��bitmap(��ǰ֧��PNG,JPGת��ɫBMP)
	 * @param resultFile
	 *            ת���ɹ����ͼƬ
	 * @return ת�������Ƿ�ɹ�
	 */
	public abstract boolean imageToMonochromeBMP(Bitmap sourceBitmap,
			File targetFile);
}
