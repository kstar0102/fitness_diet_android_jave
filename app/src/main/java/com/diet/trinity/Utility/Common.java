package com.diet.trinity.Utility;

public class Common {
    private String registerUrl = "http://192.168.109.36/api/register";
    private String loginUrl = "http://192.168.109.36/api/login";
    private String foodUrl = "http://192.168.109.36/api/fooditems";
    private String settingUrl = "http://192.168.109.36/api/settings";

    private static Common instance = new Common();

    public static Common getInstance()
    {
        return instance;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getFoodUrl(){
        return foodUrl;
    }

    public String getRegisterUrl(){
        return registerUrl;
    }

    public String getSettingUrl(){return settingUrl;}
}

