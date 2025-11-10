package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;

// ❌ REMOVE any custom `findById(long id)` you added.
// ✅ Let CrudRepository provide: Optional<UserRecord> findById(Long id)
public interface UserRepository extends CrudRepository<UserRecord, Long> {
}