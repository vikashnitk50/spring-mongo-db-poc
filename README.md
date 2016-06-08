# spring-mongo-db-poc

This project will support multi-tenancy in Mongo DB[Spring Data+Spring AOP].

Support, you are working on Webservice/Web.

1. A client will send tenant id as request header.

2. Need to write one Servlet Filter, which will load the tenand information from the DB/Cache, This filter will set the tenant schema   name for each request.
   
3. MongoMultiTenancyInterceptor will get called for each ORM/DAO/Repository, setting the tenant schema name in current thread.
   MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(dbSchemaName);
4. MultiTenantMongoDbFactory will internaly handly the multitenancy.
