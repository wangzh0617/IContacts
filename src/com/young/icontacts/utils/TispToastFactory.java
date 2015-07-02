package com.young.icontacts.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class TispToastFactory {

	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	/**
	 * 
	 * @param context
	 * @param s
	 * @param time
	 * 0/1
	 */
	public static void showToast(Context context, String s, int time) {
		if (toast == null) {
			toast = Toast.makeText(context, s, time);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > time) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(Context context, int resId, int time) {
		showToast(context, context.getString(resId), time);
	}

}