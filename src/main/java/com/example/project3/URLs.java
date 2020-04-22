package com.example.project3;

public class URLs
{
    // URL-тата!!!
    // ИП-то трябва да е същото както в xml/network_security_config.xml !

    public static final String URL_USERS = "http://192.168.1.3/Android/Api2_users.php"; // GET
    private static final String ROOT_URL = "http://192.168.1.3/Android/Api_users.php?apicall="; // POST
    // различните POST
    public static final String URL_UNDODELETE = ROOT_URL + "undodelete";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_USER_EDIT = ROOT_URL + "edit";
    public static final String URL_USER_DELETE = ROOT_URL + "delete";


    // ПО СЪЩИЯ НАЧИН ДРУГИТЕ
    public static final String URL_BOTTLES = "http://192.168.1.3/Android/Api2_bottles.php";
    private static final String ROOT_URL2 = "http://192.168.1.3/Android/Api_bottles.php?apicall=";
    public static final String URL_BOTTLE_UNDODELETE = ROOT_URL2 + "undodelete";
    public static final String URL_BOTTLE_DELETE = ROOT_URL2 + "delete";
    public static final String URL_BOTTLE_EDIT = ROOT_URL2 + "edit";
    public static final String URL_BOTTLE_ADD = ROOT_URL2 + "create";


    public static final String URL_GRAPES = "http://192.168.1.3/Android/Api2_grapes.php";
    private static final String ROOT_URL3 = "http://192.168.1.3/Android/Api_grapes.php?apicall=";
    public static final String URL_GRAPE_UNDODELETE = ROOT_URL3 + "undodelete";
    public static final String URL_GRAPE_DELETE = ROOT_URL3 + "delete";
    public static final String URL_GRAPE_EDIT = ROOT_URL3 + "edit";
    public static final String URL_GRAPE_ADD = ROOT_URL3 + "create";
    public static final String URL_GRAPE_INFO = ROOT_URL3 + "info";


    public static final String URL_WINES = "http://192.168.1.3/Android/Api2_wines.php";
    private static final String ROOT_URL4 = "http://192.168.1.3/Android/Api_wines.php?apicall=";
    public static final String URL_WINE_UNDODELETE = ROOT_URL4 + "undodelete";
    public static final String URL_WINE_DELETE = ROOT_URL4 + "delete";
    public static final String URL_WINE_EDIT = ROOT_URL4 + "edit";
    public static final String URL_WINE_ADD = ROOT_URL4 + "create";


    public static final String URL_BOTTLING = "http://192.168.1.3/Android/Api2_bottling.php";
    private static final String ROOT_URL5 = "http://192.168.1.3/Android/Api_bottling.php?apicall=";
    public static final String URL_BOTTLING_UNDODELETE = ROOT_URL5 + "undodelete";
    public static final String URL_BOTTLING_DELETE = ROOT_URL5 + "delete";
    public static final String URL_BOTTLING_EDIT = ROOT_URL5 + "edit";
    public static final String URL_BOTTLING_ADD = ROOT_URL5 + "create";



    public static final String URL_INFO_COUNT = "http://192.168.1.3/Android/Api_Info.php?apicall=info"; // POST - INFO
    public static final String URL_INFO_NOTIFICATION = "http://192.168.1.3/Android/Api2_Notification.php"; // GET
}