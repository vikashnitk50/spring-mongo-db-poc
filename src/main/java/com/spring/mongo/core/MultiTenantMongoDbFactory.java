package com.spring.mongo.core;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver.IndexDefinitionHolder;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.util.Assert;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {

  private final String defaultDBName;

  private MongoTemplate mongoTemplate;

  private static boolean INDEXING_REQUIRED = Boolean.TRUE;

  private static final Logger LOG = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);

  private static final InheritableThreadLocal<String> MONGO_DB_NAME = new InheritableThreadLocal<String>();

  private static final HashMap<String, Object> DATA_BASE_INDEX_MAP = new HashMap<String, Object>();

  public MultiTenantMongoDbFactory(final MongoClient mongoClient, final String defaultDatabaseName) {
    super(mongoClient, defaultDatabaseName);
    LOG.debug("Instantiating " + MultiTenantMongoDbFactory.class.getName() + " with default database name: "
        + defaultDatabaseName);
    this.defaultDBName = defaultDatabaseName;
  }

  public void setMongoTemplate(final MongoTemplate mongoTemplate) {
    Assert.isNull(this.mongoTemplate, "You can set MongoTemplate just once");
    this.mongoTemplate = mongoTemplate;
  }

  public static void setDatabaseNameForCurrentThread(final String databaseName) {
    LOG.debug("Switching to database: " + databaseName);
    MONGO_DB_NAME.set(databaseName);
  }

  public static void clearDatabaseNameForCurrentThread() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Removing database [" + MONGO_DB_NAME.get() + "]");
    }
    MONGO_DB_NAME.remove();
  }

  @Override
  public DB getDb() {
    final String tlName = MONGO_DB_NAME.get();
    final String dbToUse = (tlName != null ? tlName : this.defaultDBName);
    if (INDEXING_REQUIRED) {
      LOG.debug("Acquiring database: " + dbToUse);
    }
    createIndexIfNecessaryFor(dbToUse);
    return super.getDb(dbToUse);
  }

  private void createIndexIfNecessaryFor(final String database) {
    if (this.mongoTemplate == null) {
      LOG.error("MongoTemplate is null, will not create any index.");
      return;
    }
    boolean needsToBeCreated = false;
    synchronized (MultiTenantMongoDbFactory.class) {
      final Object syncObj = DATA_BASE_INDEX_MAP.get(database);
      if (syncObj == null) {
        DATA_BASE_INDEX_MAP.put(database, new Object());
        needsToBeCreated = true;
      }
    }
    synchronized (DATA_BASE_INDEX_MAP.get(database)) {
      if (needsToBeCreated) {
        LOG.debug("Creating indices for database name=[" + database + "]");
        createIndexes();
        LOG.debug("Done with creating indices for database name=[" + database + "]");
      }
    }
  }

  private void createIndexes() {
    final MongoMappingContext mappingContext = (MongoMappingContext) this.mongoTemplate.getConverter()
        .getMappingContext();
    final MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);
    for (BasicMongoPersistentEntity<?> persistentEntity : mappingContext.getPersistentEntities()) {
      checkForAndCreateIndexes(indexResolver, persistentEntity);
    }
  }

  private void checkForAndCreateIndexes(final MongoPersistentEntityIndexResolver indexResolver,
      final MongoPersistentEntity<?> entity) {
    if (entity.findAnnotation(Document.class) != null) {
      for (IndexDefinitionHolder indexDefinitionHolder : indexResolver.resolveIndexForEntity(entity)) {
        this.mongoTemplate.indexOps(entity.getType()).ensureIndex(indexDefinitionHolder);
      }
    }
  }
}
