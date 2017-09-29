package com.arlo.user.dao;

import com.arlo.user.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author khaled
 */

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByUsername(String username);
}
