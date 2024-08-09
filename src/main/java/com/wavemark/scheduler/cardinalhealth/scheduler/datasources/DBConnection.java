package com.wavemark.scheduler.cardinalhealth.scheduler.datasources;

import com.wavemark.scheduler.cardinalhealth.scheduler.security.Decryption;
import com.wavemark.scheduler.cardinalhealth.scheduler.utility.Settings;

public class DBConnection
{
  private static String SCHEDULER_KEY = "39@Mw0%1aj5^h*56";
  
  public static String getPassword()
    throws Exception
  {
    String encryptedPassword = Settings.getSettingValue("data_source_encrypted_password");
    String uschedulerPassword = Decryption.decryptAESToStr(encryptedPassword, SCHEDULER_KEY);
    return uschedulerPassword;
  }
}
