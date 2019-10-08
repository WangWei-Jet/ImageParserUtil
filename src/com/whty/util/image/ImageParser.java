package com.whty.util.image;

import java.io.File;

import android.graphics.Bitmap;

/**
 * 
 * @author 王维
 * 
 */
public abstract class ImageParser {
	public String BG_BLACK = "background_black";
	public String BG_WHITE = "background_white";
	public String BG_COLOR = BG_BLACK;

	public abstract void setBMPBackColor(String backColor);

	/**
	 * 图片转单色BMP
	 * 
	 * @param sourceBitmap
	 *            原图片的bitmap(当前支持PNG,JPG转单色BMP)
	 * @return 转换成功后的单色BMP
	 */
	public abstract byte[] imageToMonochromeBMP(Bitmap sourceBitmap);

	/**
	 * 图片转单色BMP
	 * 
	 * @param sourceBitmap
	 *            原图片的bitmap(当前支持PNG,JPG转单色BMP)
	 * @param path
	 *            转换成功后图片的存放位置
	 * @return 转换操作是否成功
	 */
	public abstract boolean imageToMonochromeBMP(Bitmap sourceBitmap,
			String path);

	/**
	 * 图片转单色BMP
	 * 
	 * @param sourceBitmap
	 *            原图片的bitmap(当前支持PNG,JPG转单色BMP)
	 * @param resultFile
	 *            转换成功后的图片
	 * @return 转换操作是否成功
	 */
	public abstract boolean imageToMonochromeBMP(Bitmap sourceBitmap,
			File targetFile);
}
