package com.simple.calculator.roku1sub;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {
    public Activity activity;

    public static PermissionUtils instance;

    public static PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    public void init(Activity activity) {
        this.activity = activity;
        if (isStoragePermissionGranted()) {
            //do nothing
        } else {
            requesttoragePermission(1024);
        }
    }

    public boolean checkPermission(String[] arrPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : arrPermission) {
                if (activity.checkSelfPermission(s) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void requestPermission(String[] arrPermission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(arrPermission, requestCode);
        }
    }

    private boolean isStoragePermissionGranted() {
        return checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS});
    }

    private void requesttoragePermission(int requestCode) {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS}, requestCode);
    }
}
