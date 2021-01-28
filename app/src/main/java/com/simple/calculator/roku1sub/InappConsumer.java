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

public class InappConsumer implements PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {
    private Context context;
    private BillingClient billingClient;
    private String lifeTime;
    private List<String> sku;
    private List<SkuDetails> skuDetails;
    private String price;

    private OnPurchaseListener onPurchaseListener;


    private static InappConsumer inStance;

    private InappConsumer(Context context) {
        this.context = context;
    }

    public static InappConsumer getInstance(Context context) {
        if (inStance == null) {
            inStance = new InappConsumer(context);
        }
        return inStance;
    }

    public void initializeInapp() {
        price = "199.99$";
        lifeTime = "yearly";
        sku = new ArrayList<>();
        skuDetails = new ArrayList<>();
        sku.add(lifeTime);
        billingClient = BillingClient.newBuilder(context).setListener(this).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                getAvaibleProduct();
                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
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
                        if ((purchase.getSku().equals(lifeTime))
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
            params.setSkusList(sku).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        skuDetails = list;
                        if (skuDetails != null && skuDetails.size() >= 1) {
                            price = skuDetails.get(0).getPrice();
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
            onPurchaseListener.buyFalseConsume();
        }
    }


    public String getPrice() {
        return price;
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
        if (purchase.getSku().equals(lifeTime)) {
            PrefHelper.getInstance().put("vip", "vip", 1);
        } else {
            PrefHelper.getInstance().put("vip", "vip", 0);
        }
        onPurchaseListener.buySuccessConsume();
    }

    public void buySubcription(Activity activity) {
        if (billingClient.isReady()) {
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails.get(0))
                    .build();
            billingClient.launchBillingFlow(activity, flowParams);
        }
    }

    public interface OnPurchaseListener {
        void buySuccessConsume();

        void buyFalseConsume();
    }

    public void setOnPurchaseListener(OnPurchaseListener onPurchaseListener) {
        this.onPurchaseListener = onPurchaseListener;
    }
}
