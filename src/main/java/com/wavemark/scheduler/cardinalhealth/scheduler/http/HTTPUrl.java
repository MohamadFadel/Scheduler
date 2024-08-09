package com.wavemark.scheduler.cardinalhealth.scheduler.http;

import com.wavemark.scheduler.cardinalhealth.scheduler.utility.Settings;

public class HTTPUrl {
    public static String getAPIJobServerURL(String methodPath)
    {
        return getServerURL() + "/wm-ws/" + methodPath;
    }

    public static String getAPPJobServerURL(String actionName)
    {
        return getServerURL() + "/quartz/"+actionName+".action";
    }

    private static String getServerURL()
    {
        return Settings.getSettingValue("server.listen.address");
    }}
