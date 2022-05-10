/*
package com.apps.freeroadingdriver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.utils.ConnectionDetector;
import com.apps.freeroadingdriver.utils.DialogUtil;
import com.apps.freeroadingdriver.utils.PurchaseHelper;
import java.util.ArrayList;
import java.util.List;


public class Subscribe extends AppCompatActivity implements View.OnClickListener {
    PurchaseHelper purchaseHelper;
    boolean isPurchaseQueryPending;
    List<Purchase> purchaseHistory;
    private ImageView imgBack;
    private LinearLayout lFreeSubscribe, lPaySubscribe;
    private boolean isSubscribe = true, isFree = true, isMonthly = false;
    private TextView txtBasic, txtFreeCast, txtPrice, txtMonthly, txtSubscribeNow, txt_skip;
    private int subscribeStatus = 1;
    private String TAG = "Subscribe", userId, transactionId, status, message;
    private ConnectionDetector cd;
    private boolean isConnected = false;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        cd = new ConnectionDetector();
        isConnected = cd.isConnectingToInternet(this);
        purchaseHelper = new PurchaseHelper(this, getPurchaseHelperListener());

        if (purchaseHelper != null && purchaseHelper.isServiceConnected())
            purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS);
        findId();

        */
/* bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsMXV6nyVQczFoWxUZdFdTPyexK2f8D/OSnusSpkbBfqf2kpkRW0Sj4584fqQf7gV660YQyL24Xt+0QKjQPO6VZjASqyZGmMRRS5VakivOTIt3z/kAVplxac21EnftROUQrbzqLKbNJyoYSbCb+f4zVP/2Zl/HRYytlclH1q+1zIuC7whRkXD3LE3L3/2pk8zFrR/43T+My+bMC1ziDmZTWYM1qE5SkQUoXU34L5TOJRzIcVqX8nmkefS7TzkJfekQb8jFHsayZu4VRQDbRzTuOxzSzv9h4Fwu+8sTKVfTIAi2gJbx5kq7jOBHNwHMyCKZx9cIFK3Jix65ufdAdhSAQIDAQAB", this);
        bp.initialize();*//*

    }

    private void findId() {
        imgBack = findViewById(R.id.img_sub_back);
        lFreeSubscribe = findViewById(R.id.l_free_subscribe);
        lPaySubscribe = findViewById(R.id.l_monthly_subscribe);
        txtBasic = findViewById(R.id.txt_basic);
        txtFreeCast = findViewById(R.id.txt_free_cast);
        txtPrice = findViewById(R.id.txt_price);
        txtMonthly = findViewById(R.id.txt_monthly);
        txtSubscribeNow = findViewById(R.id.txt_subscribe_now);
        txt_skip = findViewById(R.id.txt_skip);

        lFreeSubscribe.setOnClickListener(this);
        txtSubscribeNow.setOnClickListener(this);
        lPaySubscribe.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txt_skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_sub_back:
                onBackPressed();
                break;
            case R.id.l_free_subscribe:
                */
/*if (!isFree) {
                    subscribeStatus = 1;
                    isFree = true;
                    isMonthly = false;
                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.white));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.white));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.black));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.black));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_active);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                }*//*


              */
/*  if (isSubscribe){
                    subscribeStatus=2;
                    isSubscribe=false;
                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_active);
                }
                else {
                    subscribeStatus=1;
                    isSubscribe=true;
                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_active);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                }*//*


                break;
            case R.id.l_monthly_subscribe:
               */
/* if (!isMonthly) {
                    purchaseHelper.launchBillingFLow(BillingClient.SkuType.SUBS, "nanny_hunt");

                    subscribeStatus = 2;
                    isFree = false;
                    isMonthly = true;

                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.black));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.black));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.white));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this, R.color.white));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_active);
                }*//*


               */
/* if (isSubscribe){
                    subscribeStatus=2;
                    isSubscribe=false;
                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_active);
                }
                else {
                    subscribeStatus=1;
                    isSubscribe=true;
                    txtFreeCast.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtBasic.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.white));
                    txtPrice.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    txtMonthly.setTextColor(ContextCompat.getColor(Subscribe.this,R.color.black));
                    lFreeSubscribe.setBackgroundResource(R.drawable.subscribe_active);
                    lPaySubscribe.setBackgroundResource(R.drawable.subscribe_deactive);
                }*//*

                break;
            case R.id.txt_subscribe_now:
                */
/*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                Subscribe.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*//*


                purchaseHelper.launchBillingFLow(BillingClient.SkuType.SUBS, "freeroading_subscription");

                break;
            case R.id.txt_skip:
                startActivity(new Intent(Subscribe.this, DashboardActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public PurchaseHelper.PurchaseHelperListener getPurchaseHelperListener() {
        return new PurchaseHelper.PurchaseHelperListener() {
            @Override
            public void onServiceConnected(int resultCode) {
                Log.d(TAG, "onServiceConnected: " + resultCode);
                purchaseHelper.getPurchasedItems(BillingClient.SkuType.SUBS);

            }

            @Override
            public void onSkuQueryResponse(List<SkuDetails> skuDetails) {

                Log.d(TAG, "onSkuQueryResponse: " + skuDetails.get(0).getSku());
                Log.d(TAG, "getSubscriptionPeriod: " + skuDetails.get(0).getSubscriptionPeriod());
            }

            @Override
            public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {
                purchaseHistory = purchasedItems;
                if (purchaseHistory.size() > 0) {
                    List<String> skuList = new ArrayList<>();
                    // To make the request to get the pending SkuDetails
                    skuList.add(purchasedItems.get(0).getOrderId());
                    skuList.add(purchasedItems.get(0).getPurchaseToken());
                    skuList.add(purchasedItems.get(0).getOriginalJson());
                    skuList.add(purchasedItems.get(0).getSignature());
                    skuList.add(purchasedItems.get(0).getSku());
                    skuList.add(purchasedItems.get(0).getSignature());
                    skuList.add(purchasedItems.get(0).getPackageName());
                    // skuList.add(""+purchasedItems.get(0).getPurchaseTime());
                    purchaseHelper.getSkuDetails(skuList, BillingClient.SkuType.SUBS);
                    Log.d(TAG, "onPurchasehistoryResponse: " + purchasedItems.get(0).getOrderId());
                }
            }

            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

                Log.d(TAG, "onPurchasesUpdated: " + responseCode);
                if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
                    transactionId = purchases.get(0).getOrderId();
                    if (isConnected) {
                        //      getSubscribeData(transactionId);
                    }
                    DialogUtil.showToastLongLength(Subscribe.this, "Success");
                } else {
                    DialogUtil.showToastLongLength(Subscribe.this, "Error");
                }
            }
        };
    }


*/
/*    private void getSubscribeData(String transactionId){
        HashMap<String, String> param=new HashMap<>();
        param.put("userId",userId);
        param.put("transactionId",transactionId);
        progressDialog = CommonMethod.showProgressDialog(progressDialog, this, getString(R.string.please_wait));
        Call<NannyListModel> call = api.parentSubscribe(param);
        call.enqueue(new Callback<NannyListModel>() {
            @Override
            public void onResponse(Call<NannyListModel> call, Response<NannyListModel> response) {
                CommonMethod.hideProgressDialog(progressDialog);
                if (response.isSuccessful()){
                    status=response.body().getStatus();
                    message=response.body().getMessage();
                    if (status.equalsIgnoreCase("1")){
                        CommonMethod.showToastShort(message,Subscribe.this);
                        BMSPrefs.putString(Subscribe.this, Constants.SUBSCRIBE_TYPE, "1");
                        Intent intentHome = new Intent(Subscribe.this, MainActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        finish();
                        Subscribe.this.overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    }
                    else {
                        CommonMethod.showToastShort(message,Subscribe.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<NannyListModel> call, Throwable t) {
                CommonMethod.hideProgressDialog(progressDialog);
                Log.d("logError", t.getMessage().toString());
            }
        });

    }*//*


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (purchaseHelper != null)
            purchaseHelper.endConnection();
    }

}
*/
