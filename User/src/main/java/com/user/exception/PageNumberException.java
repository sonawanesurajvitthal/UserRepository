package com.user.exception;

import com.user.constants.ApiConstants;

public class PageNumberException extends RuntimeException{
    public PageNumberException(){
        super(ApiConstants.PAGE_NUMBER_VALIDATION);
    }

    public PageNumberException(String message){
        super(message);
    }
}
