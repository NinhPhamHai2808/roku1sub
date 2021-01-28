package com.simple.calculator.roku1sub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

public class Utils {
    public static void openPolicy(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        context.startActivity(browserIntent);
    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void openSub(Context context) {
        int vip = PrefHelper.getInstance().get("vip", "vip", 0);
        if (vip == 0) {
            Intent intent = new Intent(context, RokuSubOneActivity.class);
            context.startActivity(intent);
            return;
        } else {
            Log.d("vip", "openSub: vip");
        }
    }
}
