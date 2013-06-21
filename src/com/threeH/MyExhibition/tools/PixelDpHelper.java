package com.threeH.MyExhibition.tools;

import android.content.Context;
import android.util.DisplayMetrics;

public class PixelDpHelper {

	private static PixelDpHelper sInstance;

	public static PixelDpHelper getInstance() {
		if (sInstance == null) {
			sInstance = new PixelDpHelper();
		}
		return sInstance;
	}

	/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final float scale = dm.density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
