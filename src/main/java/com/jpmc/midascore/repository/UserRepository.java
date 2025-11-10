package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserRecord, Long> {
    // REMOVE: UserRecord findById(long id);
    // Use the inherited Optional<UserRecord> findById(Long id)
}