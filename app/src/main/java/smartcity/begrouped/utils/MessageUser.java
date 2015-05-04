package smartcity.begrouped.utils;

import java.util.HashMap;

/**
 * Created by LENOVO on 03/05/2015.
 */
public class MessageUser {
    public final static HashMap<String,String> EnglishMessages=new HashMap<>();

    public static void init(){
        EnglishMessages.put("0000","An unkown error has been encountered");
        EnglishMessages.put("0001","Network problem ! please ensure that you're connected to internet");
        EnglishMessages.put("0002","You're not connected to internet ! please connect to internet");


        EnglishMessages.put("1101","Please sign in"); //disconnected
        EnglishMessages.put("1102","There is no user using this username");
        EnglishMessages.put("1103","Invalid username or password");
        EnglishMessages.put("1104","Account already exists, please change the username");
        EnglishMessages.put("1105","Please enter your username and your password");
        EnglishMessages.put("1106","All fields should be completed");
        EnglishMessages.put("1107","Please confirm your password correctly");
        EnglishMessages.put("1108","Please accept the terms and conditions of use");



        EnglishMessages.put("1201","There is no group");
        EnglishMessages.put("1202","There is no group with this name");
        EnglishMessages.put("1203","You should be in the group to perform this action");
        EnglishMessages.put("1204","You're Already in this group");
        EnglishMessages.put("1205","You've Already demanded to join this group, but not yet accepted");
        EnglishMessages.put("1206","You must be the superviser of the group to perform this action");
        EnglishMessages.put("1207","You're the superviser of the group, you cannot exit the group");
        EnglishMessages.put("1208","Group already exists, please change the groupname");
        EnglishMessages.put("1209","Please enter the group name");
        EnglishMessages.put("1210","All feilds must be completed");

        EnglishMessages.put("2101","Your registration was successuful");

        EnglishMessages.put("2201","Your demand has been sent");
        EnglishMessages.put("2202","The group has been successfully created");

    }

    public static String get(String S)
    {
        return EnglishMessages.get(S);
    }


}
