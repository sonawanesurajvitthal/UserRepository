package com.user.exception;

import com.user.constants.ApiConstants;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(){
        super(ApiConstants.USER_ID_NOT_FOUND);
    }

    public UserIdNotFoundException(String message){
        super(message);
    }
}