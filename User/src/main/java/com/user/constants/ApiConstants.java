package com.user.constants;

public class ApiConstants {

    private ApiConstants(){}

    // Exceptions
    public static final String FIELD_REQUIRED = "Field Required";
    public static final String USER_NOT_ACTIVATED = "USER not active";
    public static final String USER_ID_NOT_FOUND = "User ID Not Found ID: ";
    public static final String NOT_FOUND = "Not Found";

    //API Endpoints
    public static final String USER_CONTROLLER = "/api/v1/user";
    public static final String LOANS_FETCHED_BY_USERID="/userId/{id}";

    //Other
    public static final String DELETE_SUCCESSFULLY = "User Deleted Successfully.";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_FETCH= "User fetched successfully";
    public static final String USER_UPDATE= "User UPDATE successfully";
    public static final String ALL_USER_FETCH= "ALL Users fetched successfully";
    public static final String SLASH = "/";


}
