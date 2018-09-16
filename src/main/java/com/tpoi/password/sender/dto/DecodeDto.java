package com.tpoi.password.sender.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class DecodeDto
{
  @NotNull
  private String encodedData;
  @NotNull
  private UUID cipherKeyId;
}

