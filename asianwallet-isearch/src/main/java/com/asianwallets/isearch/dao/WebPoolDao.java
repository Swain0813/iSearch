package com.asianwallets.isearch.dao;
import com.asianwallets.common.entity.WebPool;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *搜索网站池表的数据层
 */
public interface WebPoolDao extends MongoRepository<WebPool, String> {
}
