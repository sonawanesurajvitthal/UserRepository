package com.user.exception;

import com.user.constants.ApiConstants;

public class UserRequiredFields extends RuntimeException{

    public UserRequiredFields(){
        super(ApiConstants.FIELD_REQUIRED);
    }

    public UserRequiredFields(String message){
        super(message);
    }
}
