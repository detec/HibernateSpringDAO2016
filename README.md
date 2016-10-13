# Hibernate Spring DAO 2016 #

This is a template project to build with Maven for DAO layer and REST service. Its core components are:

- Hibernate 5.2;
- Spring 4.3;
 Jackson Databind 2.8 with JSR310 support;
 - sl4j/Logback
- MySQL 5;
- Tomcat 8;
- Java 8.

DAO layer is built with JPA 2 Entity Manager to provide loose coupling with Hibernate 5. DAO and service layers are implemented as generic as possible to avoid excess implementation classes for every entity. DAO layer provides additional methods with JPA 2 Criteria API to construct fine-grained collection queries. If your business logic is simple you can avoid creating specific service implementations of GenericService interface and inject it directly into REST controller classes.

To simplify REST service creation this template project contains pre-configured web.xml both with Spring root context configuration files and Spring MVC 4 configuration file. Sample rest controller supports exception handling and JSON pretty printing.

To test this template deploy it to Apache Tomcat and call endpoint GET http://localhost:8080/sample-dao/test . It should return 'true' in response body. You can also use additional endpoint to check current server time: GET http://localhost:8080/sample-dao/now . Requests should be accompanied by "Accept" and "Content-Type" headers with value application/json. 