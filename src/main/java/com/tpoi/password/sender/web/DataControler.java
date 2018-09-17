package com.tpoi.password.sender.web;

import com.tpoi.password.sender.bean.DecodedData;
import com.tpoi.password.sender.bean.EncodedData;
import com.tpoi.password.sender.dao.CipherKeyRepository;
import com.tpoi.password.sender.dto.EncodeDto;
import com.tpoi.password.sender.entity.CipherKeyEntity;
import com.tpoi.password.sender.exception.DecryptException;
import com.tpoi.password.sender.exception.EncryptException;
import com.tpoi.password.sender.exception.ResourceNotFoundException;
import com.tpoi.password.sender.service.CipherService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class DataControler
{
  private final CipherKeyRepository cipherKeyRepository;
  
  @Autowired
  public DataControler(final CipherKeyRepository cipherKeyRepository)
  {
    this.cipherKeyRepository = cipherKeyRepository;
  }
  
  @PostMapping("/encode")
  public EncodedData encodeAndSaveCipherKey(@RequestBody EncodeDto dataToEncode) throws EncryptException
  {
    String cipherKeyValue = RandomStringUtils.randomAlphanumeric(25);
    UUID cipherKeyId = UUID.randomUUID();
    cipherKeyRepository.save(CipherKeyEntity.builder().cipherKeyValue(cipherKeyValue).cipherKeyId(cipherKeyId).build());
    return  EncodedData.builder().cipherKeyId(cipherKeyId).encodedData(CipherService.encryptAndBase64Encode(dataToEncode.getDataToEncode(),cipherKeyValue)).build();
  }
  
  
  @GetMapping("/decode")
  public DecodedData decodeDataAndReturnClearValue(@RequestParam String encodedData,  @RequestParam UUID cipherKeyId) throws
                                                                                                       DecryptException
  {
    Optional<CipherKeyEntity> cipherKeyEntityOpt = cipherKeyRepository.findByCipherKeyId(cipherKeyId);
    CipherKeyEntity cipherKeyEntity = cipherKeyEntityOpt.orElseThrow(ResourceNotFoundException::new);
    cipherKeyRepository.delete(cipherKeyEntity);
    return DecodedData.builder().clearPassword(CipherService.base64DecodeAndDecryt(encodedData,cipherKeyEntity.getCipherKeyValue())).build();
  }
}