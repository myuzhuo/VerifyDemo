package com.example.record.demo.utils;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.record.demo.system.VerifyAppLication;


import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;


/**
 * 常见代码封装
 */
public class CodeUtil {



    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String createString(String message, Object... args) {
        return args == null || args.length == 0 ? message : String.format(Locale.getDefault(), message, args);
    }

    public static String getStackTrace(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static String getStringFromResource(@StringRes int stringId) {
        return VerifyAppLication.getInstance().getString(stringId);
    }

    public static String getStringFromResource(@StringRes int stringId, Object... formatArgs) {
        return VerifyAppLication.getInstance().getString(stringId, formatArgs);
    }

    public static int getColorFromResource(@ColorRes int colorRes) {
        return ContextCompat.getColor(VerifyAppLication.getInstance(), colorRes);
    }

    public static Bitmap getBitmapFromResource(Resources resources, @DrawableRes int resId) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeResource(resources, resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static int getDimensionFromResource(@DimenRes int dimenRes) {
        return VerifyAppLication.getInstance().getResources().getDimensionPixelSize(dimenRes);
    }

    public static double getAdjustValue(double value, double min, double max) {
        if (max > min) {
            value = Math.max(min, value);
            value = Math.min(max, value);
        }
        return value;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }


    /**
     * 调用浏览器下载App
     *
     * @param context
     * @param url
     */
    public static void downloadAppByWebBrowser(Context context, String url) {
        try {
            if (context != null && !TextUtils.isEmpty(url)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取应用程序名称
     */

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 验证手机号码是否合法.
     * 1开头11位数字
     */
    public static boolean isValidMobile(String mobiles) {
        return mobiles != null && TextUtils.isDigitsOnly(mobiles.trim()) && mobiles.trim().startsWith("1") && mobiles.trim().length() == 11;
    }

    public static void expand(final View view, final int top, final int bottom, final int left, final int right) {
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }



    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager localInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            IBinder localIBinder = activity.getCurrentFocus().getWindowToken();
            localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
        }
    }



    /**
     * compa for api<21
     */
    public static class MySpannableStringBuilder extends SpannableStringBuilder {

        public SpannableStringBuilder append(CharSequence text, Object what) {
            int start = length();
            append(text);
            setSpan(what, start, length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return this;
        }

    }

}
