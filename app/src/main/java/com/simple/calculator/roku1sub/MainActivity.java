package com.simple.calculator.roku1sub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean isPemium()
    {
        if(PrefHelper.getInstance().get("vip","vip",0)==0)
        {
            return false;
        }
        return true;
    }

    public void purchar()
    {
        PrefHelper.getInstance().put("vip","vip",1);
    }
}