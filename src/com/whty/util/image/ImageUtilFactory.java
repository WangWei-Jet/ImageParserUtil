package com.whty.util.image;

/**
 * 
 * @author ��ά
 * 
 */
public class ImageUtilFactory {
	private static ImageParser imageParser;

	public static ImageParser getImageParser() {
		if (imageParser == null) {
			imageParser = new ImageParseImp();
		}
		return imageParser;
	}
}
