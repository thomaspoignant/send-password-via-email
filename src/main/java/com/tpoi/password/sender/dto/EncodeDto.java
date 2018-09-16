package com.tpoi.password.sender.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EncodeDto
{
  @NotNull
  private String dataToEncode;
}
