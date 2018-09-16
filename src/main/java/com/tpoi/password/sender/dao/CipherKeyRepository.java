package com.tpoi.password.sender.dao;

import com.tpoi.password.sender.entity.CipherKeyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CipherKeyRepository extends MongoRepository<CipherKeyEntity, String>
{
  Optional<CipherKeyEntity> findByCipherKeyId(final UUID cipherKeyId);
}
