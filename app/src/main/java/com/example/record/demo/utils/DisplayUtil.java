package com.example.record.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;

import com.example.record.demo.system.VerifyAppLication;


public class DisplayUtil {

    public static float dpToPx(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, VerifyAppLication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * 所有字体均使用dp
     */
    public static float spToPx(float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spValue, VerifyAppLication.getInstance().getResources().getDisplayMetrics());
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        if (context != null && drawableId != 0) {
            Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
            if (drawable != null && drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable != null && drawable instanceof VectorDrawableCompat) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
        return null;
    }
}
