package com.asianwallets.task.dao;

import com.asianwallets.common.entity.Information;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InformationDao  extends MongoRepository<Information,String> {
}
