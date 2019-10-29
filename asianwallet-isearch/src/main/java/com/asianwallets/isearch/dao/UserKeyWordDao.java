package com.asianwallets.isearch.dao;

import com.asianwallets.common.entity.UserKeyWord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 关键字
 */
public interface UserKeyWordDao extends MongoRepository<UserKeyWord, String> {
}
