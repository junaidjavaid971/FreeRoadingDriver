package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class UpdateStripeRequest extends BaseRequest {
    private String stripe_account_no;
    private String stripe_refresh_token;
    private String stripe_access_token;
    private String stripe_publishable_key;
    private String stripe_scope;

    public UpdateStripeRequest(String stripe_account_no,String stripe_refresh_token,String stripe_access_token,String stripe_publishable_key,String stripe_scope) {
        super(false, true, false, false);
        this.stripe_account_no = stripe_account_no;
        this.stripe_refresh_token = stripe_refresh_token;
        this.stripe_access_token = stripe_access_token;
        this.stripe_publishable_key = stripe_publishable_key;
        this.stripe_scope = stripe_scope;
    }

}
