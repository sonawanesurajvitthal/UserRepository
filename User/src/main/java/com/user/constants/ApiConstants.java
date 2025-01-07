package com.user.constants;

public class ApiConstants {

    private ApiConstants(){}

    // Exceptions
    public static final String FIELD_REQUIRED = "Field Required";
    public static final String USER_NOT_ACTIVATED = "USER not active";
    public static final String USER_ID_NOT_FOUND = "User Not Found ID: ";
    public static final String USER_NOT_ACTIVATES = "User Not Activated: ";
    public static final String NOT_FOUND = "Not Found";

    //API Endpoints
    public static final String USER_CONTROLLER = "/api/v1/user";
    public static final String LOANS_FETCHED_BY_USERID = "/list/{id}";
    public static final String SLASH_ID = "/{id}";
    public static final String USER_LIST = "/list";
    public static final String SORT_USER_LIST = "/sortUsers/{field}";
    public static final String PAGE_USER_LIST = "/page";

    //Other
    public static final String DELETE_SUCCESSFULLY = "User Deleted Successfully.";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_FETCH= "User fetched successfully";
    public static final String USER_UPDATE= "User UPDATE successfully";
    public static final String ALL_USER_FETCH= "ALL Users fetched successfully";
    public static final String SLASH = "/";

    //validations
    public static final String ONLY_CHARACTERS = "Name can only contain letters";
    public static final String ONLY_DIGITS = "Phone can only contain Digits";
    public static final String REGEXP_ONLY_CHARACTERS = "^[a-zA-Z\\s]+$";
    public static final String REGEXP_ONLY_DIGITS = "^[0-9]+$";
    public static final String TEN_DIGITS = "Please Enter Ten Digit Phone Number.";
    public static final String PAGE_NUMBER_VALIDATION = "Page number must be greater than or equal to 1.";
    public static final String UPLOAD_FILE_FAILED = "File upload failed";


}
