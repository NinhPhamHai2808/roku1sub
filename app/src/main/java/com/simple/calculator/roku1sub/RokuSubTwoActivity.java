package com.simple.calculator.roku1sub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RokuSubTwoActivity extends Activity implements InappNoconsumer.OnPurchaseListener, InappConsumer.OnPurchaseListener {

    public TextView tvPriceTry;
    public ImageView imgTry;
    public LinearLayout btnLifeTime;
    public TextView tvLifeTime;
    public LinearLayout btnMonthly;
    public TextView tvMonthly;
    public TextView tvPolicy;
    public ImageView btnClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roku_sub_two);
        tvPriceTry = findViewById(R.id.roku_text_price);
        imgTry = findViewById(R.id.roku_btn_try);
        btnLifeTime = findViewById(R.id.roku_btn_life_time);
        tvLifeTime = findViewById(R.id.roku_tv_life_time);
        btnMonthly = findViewById(R.id.roku_btn_monthly);
        tvMonthly = findViewById(R.id.roku_tv_price_month);
        tvPolicy = findViewById(R.id.id_tv_policy);
        btnClose = findViewById(R.id.roku_close);
        String yearlyPrice = InappNoconsumer.getInstance(this).getPrice();
        String monthlyPrice = InappNoconsumer.getInstance(this).getYearlyTwo();
        String lifetimePrice = InappConsumer.getInstance(this).getPrice();


        String yearly = "Then " + yearlyPrice + "/year after trial ends";
        String monthly = monthlyPrice + "/month";
        String lifeTime = lifetimePrice + "/forever";
        String a = "- When subscribing to be our Premium subscribers, users will have Unlimited Access to use all advanced features in the Remote application\n" +
                "- Subscribed users will have an excellent experience with NO ADS disrupting while using the app, choose Roku channels with only 1 tap, particularly quickly change volume & TV channels, navigate gestures directly\n" +
                "- Subscribed users have all access to the Updated Features in the Remote when we have any new updates\n" +
                "- Access to 100+ TV channels with Premium subscriber policy enjoy convenient and superfast searching engine with mini keyboard and voice entry.";
        String b = "Subscription information in details\n" +
                "User‘s payment will be charged to your Google account at the confirmation of purchase. All new users will have a 3-DAY TRIAL and to try this subscription and a MONTHLY subscription, and you can cancel any time.\n" +
                "At your convenience,\n" +
                "- Before the end of the current period, within 24 hours if users do not cancel the subscription, an auto-renew subscription will be activated. The account will be charged for renewal within 24 hours prior to the end of the current period following Google‘s Privacy Policy & Terms of use.\n" +
                "A VIP subscription for users to be Premium after the 3-day free trial is the only " + yearlyPrice + "/year, You can cancel at any time\n" +
                "After a month of subscription, then " + monthlyPrice + "/monthly, and you can cancel at any time.\n" +
                "- Users can manage the subscriptions and turn off the auto-renewal subscription by going through the Setting app after purchase.";
        String c = "In-app subscription in details\n" +
                "Besides the yearly subscription, users can be our VIP member forever with one-time payment.\n" +
                "Experience all unlocked features and no need of paying extra charges during the time using the app with payment paid.\n" +
                "Only " + lifetimePrice + " for a lifetime subscription using Roku remote control with no time-limited.\n" +
                "Users can manage the subscriptions and cancel them any time by going through the Setting app after purchase on your Google Account in the Google Play store.";

        String policy = a + b + c;

        tvPriceTry.setText(yearly);
        tvMonthly.setText(monthly);
        tvLifeTime.setText(lifeTime);
        tvPolicy.setText(policy);

        InappNoconsumer.getInstance(this).setOnPurchaseListener(this);
        InappConsumer.getInstance(this).setOnPurchaseListener(this);

        imgTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InappNoconsumer.getInstance(RokuSubTwoActivity.this).buySubcription(RokuSubTwoActivity.this);
                } catch (Exception e) {
                }
            }
        });

        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InappNoconsumer.getInstance(RokuSubTwoActivity.this).buySubcription(RokuSubTwoActivity.this);
                } catch (Exception e) {
                }
            }
        });

        btnLifeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InappConsumer.getInstance(RokuSubTwoActivity.this).buySubcription(RokuSubTwoActivity.this);
                } catch (Exception e) {
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buySuccess();
            }
        });

    }

    @Override
    public void buySuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void buyFalse() {

    }

    @Override
    public void buySuccessConsume() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void buyFalseConsume() {

    }

    @Override
    protected void onResume() {
        InappConsumer.getInstance(this).setOnPurchaseListener(this);
        InappNoconsumer.getInstance(this).setOnPurchaseListener(this);
        super.onResume();
    }
}
