package com.young.icontacts.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.young.icontacts.R;

public class ImageViewUtils {
		
	// setImagefromUrl(s, new_head_creat)
	public static void setImagefromUrl(Context context, String img_url,
			ImageView imageView) {

		//Picasso.with(context).setDebugging(true);
		try {
			Picasso.with(context).load(img_url)
					.placeholder(R.drawable.missing_pic_round)
					.error(R.drawable.missing_pic_round).into(imageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	// setImagefromUrl(s, new_head_creat)
		public static void setImagefromUrlTwo(Context context, String img_url,
				ImageView imageView) {

			//Picasso.with(context).setDebugging(true);
			try {
				Picasso.with(context).load(img_url)
						.placeholder(R.drawable.missing_pic)
						.error(R.drawable.missing_pic).into(imageView);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		

	public static class CircleTransformation implements Transformation {
		int round;

		/**
		 * @param round
		 */
		public CircleTransformation(int round) {
			super();
			this.round = round;
		}

		@Override
		public Bitmap transform(Bitmap source) {
			return getRoundedCornerBitmap(source, round);
		}

		@Override
		public String key() {
			return "square()";
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int round) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = round;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
