# Introduction

Java Project containing the backend service for a blog application

<!-- TOC -->
* [Introduction](#introduction)
* [blog-service](#blog-service)
    * [Available API Endpoints](#available-api-endpoints)
    * [Available functionalities](#available-functionalities)
    * [Security](#security)
        * [Roles](#roles)
        * [Endpoints per role](#endpoints-per-role)
  * [Running the application in dev mode](#running-the-application-in-dev-mode)
  * [Packaging and running the application](#packaging-and-running-the-application)
<!-- TOC -->

# blog-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Available API Endpoints

This service exposes several API endpoints, to get an overview use the included OpenAPI specification file
at http://localhost:8000/q/openapi
You can also explore the API interactively using the included Swagger UI at http://localhost:8000/q/swagger-ui
Alternatively you can also view the auto generated file at [openapi.json](./api-docs/openapi.json)
Somehow the build in the pipeline fails if the autogeneration of the openapi file is enabled, so for now it is commented
out in the application.properties file.

## Available functionalities

- Create a new blog post
- Get a list of all blog posts
- Get a single blog post by id
- Create a new author
- Get a list of all authors
- Get a single author by id
- Update a blog post
- Delete a blog post
- Create a new comment for a blog post

## Security

This service will use OPENID CONNECT (OIDC) for authentication and authorization.
The service will be configured to use Keycloak as the OIDC provider. In dev mode the service will use a local Keycloak
instance which is a docker container.
Before starting the Service make sure to copy the config file for
keycloak [quarkus-realm.json](./src/main/java/hftm/joshua/config/quarkus-realm.json) to the `target/classes` folder.
After starting the service in dev mode you can use the following request to obtain a token for a user with the role "
user":

```shell script
curl -v -X POST -u "mein-client:mein-secret" -d "username=alice&password=alice&grant_type=password" http://keycloak:8180/realms/mein-realm/protocol/openid-connect/token
```

And for getting a token for a user with the role "admin":

```shell script
curl -v -X POST -u "mein-client:mein-secret" -d "username=bob&password=bob&grant_type=password" http://keycloak:8180/realms/mein-realm/protocol/openid-connect/token
```

### Roles

The following roles are available:

| Role      | Description                                                                                          |
|-----------|------------------------------------------------------------------------------------------------------|
| Anonymous | The role everyone has if they are not authenticated                                                  |
| User      | Standard Role for every user after authentication, which allows to do basic operations               |
| Admin     | Roles for users with special privileges which is only granted to specific users after authentication |  

### Endpoints per role

| Role      | Entity  | Endpoint     | Method |
|-----------|---------|--------------|--------|
| Anonymous | Author  | /author      | GET    |
| User      | Author  | /author      | POST   |
| Anonymous | Author  | /author/{id} | GET    |
| Admin     | Author  | /author/{id} | DELETE |
| Anonymous | Blog    | /blog        | GET    |
| User      | Blog    | /blog        | POST   |
| Anonymous | Blog    | /blog/{id}   | GET    |
| Admin     | Blog    | /blog/{id}   | DELETE |
| User      | Blog    | /blog/{id}   | PATCH  |
| Anonymous | Comment | /comment     | GET    |
| User      | Comment | /comment     | POST   |

Basically an anonymous user can only read data, while a user can also create new data. An admin can also delete data.
This might be extended in the future with new endpoints which allows a user to delete their own data.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Messaging
This service is also part of a distributed system and uses Kafka for messaging. Everytime we create a post over the API the service produces a message to the Kafka topic `text-validation` using an Emitter.
The message contains the id of the post and the text of the post. The consumer of this message is the [text-validator-service](https://github.com/joshua-lehmann/text-validator) which is another Quarkus service responsible for validating the text of the post.
The text-validator-service will then produce a message to the Kafka topic `text-validation-response` with the result of the validation (how is validation done [is explained here](https://github.com/joshua-lehmann/text-validator/pkgs/container/text-validator#validation-process)). The consumer of this message is this [blog-service](https://github.com/joshua-lehmann/blog-service). The message is then saved to a mysql database.

### How to run the full system with docker-compose
To run the full system with the blog-service, text-validator-service, redpanda and a mysql database you can use the docker-compose file in the root of this project by running the following command:
```shell script
docker-compose up
```
All images are public available on GitHub Container Registry. If you want you could also check out both repos and then build the images yourself and then change the image names in the docker-compose file.
