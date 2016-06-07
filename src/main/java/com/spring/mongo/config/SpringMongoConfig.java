package com.spring.mongo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.MongoClient;
import com.spring.mongo.core.MultiTenantMongoDbFactory;

@Configuration
@PropertySource("classpath:/com/spring/mongo/properties/mongodb.properties")
public class SpringMongoConfig extends AbstractMongoConfiguration {

  @Value("${mongodb.host}")
  private String host;

  @Value("#{new Integer('${mongodb.port}')}")
  private Integer port;

  @Value("${mongodb.dbName}")
  private String dbName;

  public SpringMongoConfig() {
  }

  @Override
  public MongoClient mongo() throws Exception {
    return new MongoClient(host, port);
  }

  @Override
  @Bean
  public MongoTemplate mongoTemplate() throws Exception {

    MultiTenantMongoDbFactory mongoDbFactory = (MultiTenantMongoDbFactory) mongoDbFactory();

    MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);

    mongoDbFactory.setMongoTemplate(mongoTemplate);

    return mongoTemplate;
  }

  @Override
  @Bean
  public MongoDbFactory mongoDbFactory() throws Exception {
    
    return new MultiTenantMongoDbFactory(mongo(), dbName);
    
  }

  @Override
  protected String getDatabaseName() {
    return dbName;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfig() {
    
    return new PropertySourcesPlaceholderConfigurer();
    
  }

}