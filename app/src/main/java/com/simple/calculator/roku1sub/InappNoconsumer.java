package com.simple.calculator.roku1sub;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class InappNoconsumer implements PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {
    private Context context;
    private BillingClient billingClient;
    private String yearly;
    private String yearlyTwo;
    private List<String> sku;
    private List<SkuDetails> skuDetails;
    private String price;
    private String priceTwo;

    private OnPurchaseListener onPurchaseListener;


    private static InappNoconsumer inStance;

    private InappNoconsumer(Context context) {
        this.context = context;
    }

    public static InappNoconsumer getInstance(Context context) {
        if (inStance == null) {
            inStance = new InappNoconsumer(context);
        }
        return inStance;
    }

    public void initializeInapp() {
        price = "199.99$";
        priceTwo = "199.99$";
        yearly = "yearly";
        yearlyTwo = "yearlyTwo";
        sku = new ArrayList<>();
        skuDetails = new ArrayList<>();
        sku.add(yearly);
        sku.add(yearlyTwo);
        billingClient = BillingClient.newBuilder(context).setListener(this).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                getAvaibleProduct();
                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                PrefHelper.getInstance().put("vip", "vip", 0);
                if (purchasesResult.getPurchasesList() != null) {
                    for (Purchase purchase : purchasesResult.getPurchasesList()) {
                        if (purchase.isAcknowledged()) {
                            //do nothing
                        } else {
                            handleAcknowledge(purchase);
                        }
                    }
                    for (Purchase purchase : purchasesResult.getPurchasesList()) {
                        if ((purchase.getSku().equals(yearly) || purchase.getSku().equals(yearlyTwo))
                                && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            PrefHelper.getInstance().put("vip", "vip", 1);
                        } else {
                            PrefHelper.getInstance().put("vip", "vip", 0);
                        }
                    }

                } else {
                    PrefHelper.getInstance().put("vip", "vip", 0);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //do nothing
            }
        });
    }

    private void getAvaibleProduct() {
        if (billingClient.isReady()) {
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(sku).setType(BillingClient.SkuType.SUBS);
            billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        skuDetails = list;
                        if (skuDetails != null && skuDetails.size() >= 2) {
                            price = skuDetails.get(0).getPrice();
                            priceTwo = skuDetails.get(1).getPrice();
                        } else {

                        }
                    }
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            for (Purchase purchase : list) {
                handleAcknowledge(purchase);
            }
        } else {
            onPurchaseListener.buyFalse();
        }
    }


    public String getPrice() {
        return price;
    }

    public String getYearlyTwo() {
        return priceTwo;
    }

    private void handleAcknowledge(Purchase purchase) {
        try {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                applyPurchase(purchase);
            }
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
            }
        } catch (Exception e) {
            AcknowledgePurchaseParams acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
            billingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
        } finally {
            AcknowledgePurchaseParams acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
            billingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
        }
    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

    }

    private void applyPurchase(Purchase purchase) {
        if (purchase.getSku().equals(yearly) || purchase.getSku().equals(yearlyTwo)) {
            PrefHelper.getInstance().put("vip", "vip", 1);
        } else {
            PrefHelper.getInstance().put("vip", "vip", 0);
        }
        onPurchaseListener.buySuccess();
    }

    public void buySubcription(Activity activity) {
        if (billingClient.isReady()) {
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails.get(0))
                    .build();
            billingClient.launchBillingFlow(activity, flowParams);
        }
    }

    public void buySubscriptionTwo(Activity activity) {
        if (billingClient.isReady()) {
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails.get(1))
                    .build();
            billingClient.launchBillingFlow(activity, flowParams);
        }
    }

    public interface OnPurchaseListener {
        void buySuccess();

        void buyFalse();
    }

    public void setOnPurchaseListener(OnPurchaseListener onPurchaseListener) {
        this.onPurchaseListener = onPurchaseListener;
    }
}
