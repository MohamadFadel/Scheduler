package com.cardinalhealth.scheduler.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Settings 
{
  private final static Logger logger = LogManager.getLogger(Settings.class);
  private static final String SETTINGS_LOCATION_SERVER = "/home/oracle/wavemark/deployments/wm-scheduler/settings.properties";
  private static final String SETTINGS_LOCATION_LOCAL = "C:/Development/cims-scheduler/settings.properties";
  
  private static final String QRTZ_SETTINGS_LOCATION_SERVER = "/home/oracle/wavemark/deployments/wm-scheduler/server.properties";
  private static final String QRTZ_SETTINGS_LOCATION_LOCAL = "C:/Development/cims-scheduler/server.properties";

  public static synchronized String getSettingValue(String key)
  {
    String value = null;
    Properties properties = getSettingFile();
    try
    {
      value = properties.getProperty(key);
    }
    catch (Exception e)
    {
      logger.error("Unexpected error occured:" + e);

    }
    return value;
  }

  public static synchronized Properties getSettingFile()
  {
    Properties properties = new Properties();
    FileInputStream fis = null;
    try
    {
      File settingsFile = new File(SETTINGS_LOCATION_SERVER);
      if (settingsFile.exists())
      {
        fis = new FileInputStream(SETTINGS_LOCATION_SERVER);
        properties.load(fis);
      }
      else
      {
        fis = new FileInputStream(SETTINGS_LOCATION_LOCAL);
        properties.load(fis);
      }

      fis.close();
    }
    catch (Exception e)
    {
      logger.error("Unable to load settings file in wm-scheduler" + e);
    }
    return properties;
  }
  
  public static String getQuartzSettingsFilePath()
  {
    String filePath = QRTZ_SETTINGS_LOCATION_LOCAL;

    File settingsFile = new File(QRTZ_SETTINGS_LOCATION_SERVER);
    if (settingsFile.exists())
      filePath = QRTZ_SETTINGS_LOCATION_SERVER;

    return filePath;
  }
  
}
