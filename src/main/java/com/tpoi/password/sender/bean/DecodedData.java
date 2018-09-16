package com.tpoi.password.sender.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DecodedData
{
  private String clearPassword;
}
