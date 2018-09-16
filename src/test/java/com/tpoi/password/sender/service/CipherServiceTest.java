package com.tpoi.password.sender.service;

import com.tpoi.password.sender.exception.DecryptException;
import com.tpoi.password.sender.exception.EncryptException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;

public class CipherServiceTest
{
  @Test
  public void encryptAndTestBase64() throws Exception
  {
    String encryptPwd = CipherService.encryptAndBase64Encode(RandomStringUtils.random(15),RandomStringUtils.random(35));
    Assert.assertTrue(Base64.isBase64(encryptPwd.getBytes()));
  }
  
  @Test
  public void encryptAndDecryptPassword() throws Exception
  {
    String password = RandomStringUtils.random(150);
    String cipherKey = RandomStringUtils.random(35);
    String encryptPwd = CipherService.encryptAndBase64Encode(password,cipherKey);
    String decryptPwd = CipherService.base64DecodeAndDecryt(encryptPwd,cipherKey);
    Assert.assertEquals("Password is the same after decoding",password,decryptPwd);
  }
  
  @Test(expected=DecryptException.class)
  public void encryptAnddecryptWithDifferentKey() throws Exception
  {
    String password = RandomStringUtils.random(150);
    String cipherKey = RandomStringUtils.random(35);
    String encryptPwd = CipherService.encryptAndBase64Encode(password,cipherKey);
    System.out.println(encryptPwd);
    CipherService.base64DecodeAndDecryt(encryptPwd,cipherKey+"1");
  }
  
  @Test(expected=EncryptException.class)
  public void encryptCipherKeyNull() throws Exception
  {
    CipherService.encryptAndBase64Encode(RandomStringUtils.random(150),null);
  }
  
  @Test(expected=DecryptException.class)
  public void decryptCipherKeyNull() throws Exception
  {
    CipherService.base64DecodeAndDecryt(RandomStringUtils.random(150),null);
  }
  
  @Test(expected=InvalidParameterException.class)
  public void encyrptDataNull()throws Exception
  {
    CipherService.encryptAndBase64Encode(null,RandomStringUtils.random(150));
  }
  
  @Test(expected=InvalidParameterException.class)
  public void decyrptDataNull()throws Exception
  {
    CipherService.base64DecodeAndDecryt(null,RandomStringUtils.random(150));
  }
}