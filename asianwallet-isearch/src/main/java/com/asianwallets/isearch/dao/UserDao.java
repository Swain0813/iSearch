package com.asianwallets.isearch.dao;

import com.asianwallets.common.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, String> {
}
