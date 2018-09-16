package com.tpoi.password.sender.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class CipherKeyEntity
{
  @Id
  private String id;
  @UniqueElements
  private UUID cipherKeyId;
  private String cipherKeyValue;
}
