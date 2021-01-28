package com.simple.calculator.roku1sub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RokuSplash extends Activity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_roku_splash);

        TextView tvPolicy = findViewById(R.id.id_tv_policy);
        ImageView tvAgree = findViewById(R.id.id_tv_agree);
        ImageView imgSplash = findViewById(R.id.id_img_background_splash);
        CheckBox checkBox = findViewById(R.id.id_check_policy);
        tvPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPolicy();
            }
        });
        ImageView imgIcon = findViewById(R.id.id_img_icon);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.anim_appear_down);
        imgIcon.startAnimation(zoom);

        PrefHelper.getInstance().initSharePreferencesHelper(getApplicationContext());
        InappNoconsumer.getInstance(getApplicationContext()).initializeInapp();
        InappConsumer.getInstance(getApplicationContext()).initializeInapp();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvAgree.setImageResource(getResources().getIdentifier("roku_agree", "drawable", getPackageName()));
                } else {
                    tvAgree.setImageResource(getResources().getIdentifier("roku_disable", "drawable", getPackageName()));
                }
            }
        });

        int show = PrefHelper.getInstance().get("isFirst", "isFirst", 0);
        if (show == 0) {
            tvAgree.setVisibility(View.VISIBLE);
            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        PrefHelper.getInstance().put("isFirst", "isFirst", 1);
                        checkVip();
                    } else {
                    }
                }
            });
        } else {
            checkBox.setVisibility(View.INVISIBLE);
            tvAgree.setVisibility(View.INVISIBLE);
            tvAgree.setOnClickListener(null);
            tvPolicy.setVisibility(View.INVISIBLE);
            tvPolicy.setOnClickListener(null);
            imgSplash.setImageResource(getResources().getIdentifier("roku_splash_2", "drawable", getPackageName()));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkVip();
                }
            }, 5000);
        }
        try {
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();
            firebaseRemoteConfig.setConfigSettingsAsync(settings);
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_values_default);
            firebaseRemoteConfig.fetchAndActivate();
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        } catch (Exception e) {

        }
    }

    public void openPolicy() {
        Utils.openPolicy(this);
    }


    /*public void checkVip() {
        int vip = PrefHelper.getInstance().get("vip", "vip", 0);
        if (firebaseRemoteConfig != null) {
            if (firebaseRemoteConfig.getLong(RemoteKey.SHOW_SUB) == 1) {
                if (vip == 0) {
                    Intent intent = new Intent(this, RokuSubOneActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    openMain();
                }
            } else {
                openMain();
            }
        } else {
            if (vip == 0) {
                Intent intent = new Intent(this, RokuSubOneActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                openMain();
            }
        }
    }*/

    public void checkVip()
    {
        int vip = PrefHelper.getInstance().get("vip", "vip", 0);
        if (vip == 0) {
            Intent intent = new Intent(this, RokuSubOneActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            openMain();
        }
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionUtils.getInstance().checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS})) {
            PermissionUtils.getInstance().requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS}, 1024);
        }
    }
}