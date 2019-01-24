package com.example.record.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;


import com.example.record.demo.R;
import com.example.record.demo.ui.view.CommonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PermissionUtil {

    private static final Map<String, String> PERMISSION_MAP = new HashMap<>();

    static {
//        // 存储空间
//        PERMISSION_MAP.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, CodeUtil.getStringFromResource(R.string.permission_access_external_storage));
//        PERMISSION_MAP.put(Manifest.permission.READ_EXTERNAL_STORAGE, CodeUtil.getStringFromResource(R.string.permission_access_external_storage));
//
//        // 电话
//        PERMISSION_MAP.put(Manifest.permission.READ_PHONE_STATE, CodeUtil.getStringFromResource(R.string.permission_read_phone_state));
//
//        // 相机
//        PERMISSION_MAP.put(Manifest.permission.CAMERA, CodeUtil.getStringFromResource(R.string.permission_access_camera));
//
//        // 位置信息
//        PERMISSION_MAP.put(Manifest.permission.ACCESS_COARSE_LOCATION, CodeUtil.getStringFromResource(R.string.permission_access_coarse_location));
//        PERMISSION_MAP.put(Manifest.permission.ACCESS_FINE_LOCATION, CodeUtil.getStringFromResource(R.string.permission_access_coarse_location));
//
//        //语音
//        PERMISSION_MAP.put(Manifest.permission.RECORD_AUDIO,CodeUtil.getStringFromResource(R.string.permission_record_audio));
    }

    private static final int RET_CODE_PERMISSION = 109;

    private static final int RET_CODE_SETTING_ACTIVITY = 110;

    public static final int RET_CODE_PERMISSION_ACTIVITY = 111;

    public interface PermissionGrantedCallback {
        void granted(String perm);
    }

    // 获取未授权的权限
    public static List<String> getDenyPermissions(Context context, @NonNull String... perms) {
        List<String> denyPermissions = new ArrayList<>();
        try {
            for (String perm : perms) {
                if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(perm);
                }
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        return denyPermissions;
    }

    public static void requestPermission(FragmentActivity host, String permission) {
        ActivityCompat.requestPermissions(host, new String[]{permission}, RET_CODE_PERMISSION);
    }

    public static void requestPermission(FragmentActivity host, String[] permission) {
        ActivityCompat.requestPermissions(host, permission, RET_CODE_PERMISSION);
    }

    public static void onRequestPermissionsResult(final FragmentActivity host, int requestCode,
                                                  @NonNull final String[] permissions,
                                                  @NonNull int[] grantResults, @Nullable PermissionGrantedCallback callback) {
        if (requestCode != RET_CODE_PERMISSION) {
            return;
        }
        final ArrayList<String> denideList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.granted(permissions[i]);
                }
            } else {
                denideList.add(permissions[i]);
            }
        }
        if (denideList.size() > 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(host, denideList.get(0))) {
                CommonDialog commonDialog = CommonDialog.newInstance(PERMISSION_MAP.get(denideList.get(0)), "",
                        CodeUtil.getStringFromResource(R.string.cancel),
                        CodeUtil.getStringFromResource(R.string.confirm));
                commonDialog.setCallback(new CommonDialog.Callback() {
                    @Override
                    public void doLeftClick() {
                        host.finish();
                    }

                    @Override
                    public void doRightClick() {
                        String[] array = new String[denideList.size()];
                        array = denideList.toArray(array);
                        ActivityCompat.requestPermissions(host, array, RET_CODE_PERMISSION);
                    }
                });
                commonDialog.showAllowingStateLoss(host.getSupportFragmentManager(), "");

            } else {
                CommonDialog commonDialog = CommonDialog.newInstance(PERMISSION_MAP.get(denideList.get(0)), "",
                        CodeUtil.getStringFromResource(R.string.cancel),
                        CodeUtil.getStringFromResource(R.string.confirm));
                commonDialog.setCallback(new CommonDialog.Callback() {
                    @Override
                    public void doLeftClick() {
                        host.finish();
                    }

                    @Override
                    public void doRightClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", host.getPackageName(), null);
                        intent.setData(uri);
                        host.startActivityForResult(intent, RET_CODE_SETTING_ACTIVITY);
                    }
                });
                commonDialog.showAllowingStateLoss(host.getSupportFragmentManager(), "");
            }
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data, Runnable runnable) {
        if (requestCode == RET_CODE_SETTING_ACTIVITY) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

}
