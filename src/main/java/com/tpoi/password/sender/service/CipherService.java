package com.tpoi.password.sender.service;

import com.tpoi.password.sender.exception.DecryptException;
import com.tpoi.password.sender.exception.EncryptException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CipherService
{
  
  /**
   * Encrypt the data and base64 the results.
   * @param dataToEncrypt - data to encrypt
   * @param cipherKey - the key to encode
   * @return a base64 of the encrypt data
   * @throws EncryptException
   */
  public static String encryptAndBase64Encode(String dataToEncrypt, String cipherKey) throws EncryptException
  {
    try
    {
      checkCipherKey(cipherKey);
      checkData(dataToEncrypt);
      return Base64.getUrlEncoder().encodeToString(encrypt(dataToEncrypt,cipherKey));
    }
    catch (InvalidKeyException|BadPaddingException|IllegalBlockSizeException e)
    {
      throw new EncryptException(e);
    }
  }
  
  /**
   * Decrypt the data in base64 and return clear value
   * @param dataToDecrypt the dataToDecrypt
   * @param cipherKey - the key to decode
   * @return the clear value
   * @throws DecryptException
   */
  public static String base64DecodeAndDecryt(String dataToDecrypt, String cipherKey) throws DecryptException
  {
    try
    {
      checkCipherKey(cipherKey);
      checkData(dataToDecrypt);
      return decrypt(Base64.getUrlDecoder().decode(dataToDecrypt.getBytes(StandardCharsets.UTF_8.name())),cipherKey);
    }
    catch (InvalidKeyException|BadPaddingException|IllegalBlockSizeException|UnsupportedEncodingException e)
    {
      throw new DecryptException(e);
    }
  }
  
  /**
   * Check is the cipherKey is valid
   * @param cipherKey - the cipherKey to check
   * @throws InvalidKeyException
   */
  private static void checkCipherKey(final String cipherKey) throws InvalidKeyException
  {
    if(StringUtils.isBlank(cipherKey))
    {
      throw new InvalidKeyException(String.format("Invalid cipherKey [%s]",cipherKey));
    }
  }
  
  private static void checkData(final String data) throws InvalidParameterException
  {
    if(StringUtils.isBlank(data))
    {
      throw new InvalidParameterException(String.format("Data can not be [%s]",data));
    }
  }
  

  private static byte[] encrypt (String plainText,String cipherKey) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException
  {
    try
    {
      byte[] clean = plainText.getBytes();
      
      // Generating IV.
      int ivSize = 16;
      byte[] iv = new byte[ivSize];
      SecureRandom random = new SecureRandom();
      random.nextBytes(iv);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
      
      // Hashing key.
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(cipherKey.getBytes(StandardCharsets.UTF_8.name()));
      byte[] keyBytes = new byte[16];
      System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
      SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
      
      // Encrypt.
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(clean);
      
      // Combine IV and encrypted part.
      byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
      System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
      System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);
      
      return encryptedIVAndText;
    }
    catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchPaddingException e)
    {
      //not supposed to happen, because this is fixed values
      throw new RuntimeException("Error in cipher implementation", e);
    }
    
  }
  
  
  private static String decrypt(byte[] encryptedIvTextBytes,String cipherKey) throws InvalidKeyException,
                                                            BadPaddingException, IllegalBlockSizeException
  {
    try
    {
      int ivSize = 16;
      int keySize = 16;
      
      // Extract IV.
      byte[] iv = new byte[ivSize];
      System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
      
      // Extract encrypted part.
      int encryptedSize = encryptedIvTextBytes.length - ivSize;
      byte[] encryptedBytes = new byte[encryptedSize];
      System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);
      
      // Hash key.
      byte[] keyBytes = new byte[keySize];
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(cipherKey.getBytes());
      System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
      SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
      
      // Decrypt.
      Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);
      
      return new String(decrypted);
    }
    catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException e)
    {
      //not supposed to happen, because this is fixed values
      throw new RuntimeException("Error in cipher implementation", e);
    }
  }
}
