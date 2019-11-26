package com.example.nilaksha.schoolbus.ClassFile;

/**
 * Created by Damith on 5/29/2018.
 */
public class Constant {
    private int code;
    private String msg;
    public int success=100;

    public String UserID;
    public String userType;



    public String errorMSG="Duplicate Mobile Number found";
    public String errorLogoutMSG="Logout Fail...";

    public Constant(){

    }
    public Constant(int code){
        this.code=code;
    }
    public Constant(String msg){
        this.msg=msg;
    }


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
