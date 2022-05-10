package com.apps.freeroadingdriver.model.requestModel;


import com.apps.freeroadingdriver.constants.AppConstant;

/**
 * Created by Admin on 9/26/2017.
 */

public class TermsAboutRequest {
    private String identifier;
    private String language;
    public TermsAboutRequest(String identifier) {
        this.identifier = identifier;
        this.language = AppConstant.APP_LANGUAGE;
    }
}
