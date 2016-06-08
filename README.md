# spring-mongo-db-poc

This project will support multi-tenancy in Mongo DB[Spring Data+Spring AOP].

Support, you are working on Webservice/Web.

1. A client will send tenant id as request header.

2. Need to write one Servlet Filter, which will load the tenand information from the DB/Cache, This filter will set the tenant schema   name for each request.
   
3. MongoMultiTenancyIntercepto:r This class intercepts method invocation and checks for DAO/Reposirty Type.
   It will set the Tenant Schema Name for the current thread if Target object type is DAO/ORM/Repository.

            MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(dbSchemaName);


4. MultiTenantMongoDbFactory will internaly deal with the multitenancy.
