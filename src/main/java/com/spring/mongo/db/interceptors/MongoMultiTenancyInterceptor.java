package com.spring.mongo.db.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.mongo.core.MultiTenantMongoDbFactory;
import com.spring.mongo.db.utils.Constants;

public class MongoMultiTenancyInterceptor implements MethodInterceptor {

  public Object invoke(MethodInvocation invocation) throws Throwable {

    ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;

    Object target = reflectiveMethodInvocation.getThis();

    if (target instanceof MongoRepository) {

      String dbName = MDC.get(Constants.MONGO_DB_NAME_KEY);

      MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(dbName);

      Object object = invocation.proceed();

      MultiTenantMongoDbFactory.clearDatabaseNameForCurrentThread();
      return object;

    } else {
      return invocation.proceed();
    }

  }

}
