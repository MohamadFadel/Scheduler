package com.cardinalhealth.scheduler.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Decryption
{
  private static String STATIC_IV = "3l8*74fsio0(ds4@";
  
  public static String decryptAESToStr(String encryptedText, String key)
    throws Exception
  {
    String result = "";
    try
    {
      byte[] byteDataToDecrypt = hexStringToByteArray(encryptedText);
      byte[] decryptedByte = decryptASESToByte(byteDataToDecrypt, key);
      result = new String(decryptedByte);
    }
    catch (Exception e)
    {
      throw new Exception("Failed to decrypt AES-encrypted string.", e);
    }

    return result;
  }

  private static byte[] hexStringToByteArray(String s)
  {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
    {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}