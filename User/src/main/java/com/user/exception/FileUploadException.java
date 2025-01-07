package com.user.exception;

import com.user.constants.ApiConstants;

public class FileUploadException extends RuntimeException{

    public FileUploadException(){
        super(ApiConstants.UPLOAD_FILE_FAILED);
    }

    public FileUploadException(String message){
        super(message);
    }
}
