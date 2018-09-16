package com.tpoi.password.sender.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class EncodedData
{
  private String encodedData;
  private UUID cipherKeyId;
}
