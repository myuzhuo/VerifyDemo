package com.example.record.demo.ui.view;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.example.record.demo.R;
import com.example.record.demo.system.VerifyAppLication;
import com.example.record.demo.utils.CodeUtil;

public enum TipsToast {

    INSTANCE;

    private TextView mTipToastTxt;

    private View mContentView;

    TipsToast() {
        mContentView = View.inflate(VerifyAppLication.getInstance(), R.layout.view_tips_toast, null);
        mTipToastTxt = mContentView.findViewById(R.id.tip_toast_txt);
    }

    public void showTips(String msg, int duration) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            final String msgTemp = msg;
            final int durationTemp = duration;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showTips(msgTemp, durationTemp);
                }
            });
        } else {
            showTipsImp(msg, duration);
        }
    }

    private void showTipsImp(String msg, int duration) {
        if (mContentView != null) {
            try {
                Toast toast = new Toast(VerifyAppLication.getInstance());
                toast.setView(mContentView);
                mTipToastTxt.setText(msg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(duration);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show(String msg) {
        showTips(msg, Toast.LENGTH_SHORT);
    }

    public void show(@StringRes int res) {
        showTips(CodeUtil.getStringFromResource(res), Toast.LENGTH_SHORT);
    }

    public void show(@StringRes int res, int duration) {
        showTips(CodeUtil.getStringFromResource(res), duration);
    }

    public void showThreeSeconds(String msg) {
        if (mContentView != null) {
            try {
                final Toast toast = new Toast(VerifyAppLication.getInstance());
                toast.setView(mContentView);
                mTipToastTxt.setText(msg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                new WeakHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 3 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
