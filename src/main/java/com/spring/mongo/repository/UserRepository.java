package com.spring.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.mongo.db.interceptors.PerformanceTrace;
import com.spring.mongo.documents.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  @PerformanceTrace
  @Query("{ 'name' : ?0 }")
  List<User> findUserByName(String name);

}
