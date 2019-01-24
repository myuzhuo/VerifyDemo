package com.example.record.demo.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.Field;


public class SafeDialogFragment extends DialogFragment {

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            try {
                super.show(manager, tag);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            try {
                super.dismiss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (isAdded()) {
            try {
                super.dismissAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解决java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
