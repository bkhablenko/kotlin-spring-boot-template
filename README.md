# kotlin-spring-boot-template

[![CircleCI](https://circleci.com/gh/bkhablenko/kotlin-spring-boot-template.svg?style=shield)](https://circleci.com/gh/bkhablenko/kotlin-spring-boot-template)

## How to run

```bash
./gradlew bootRun
```

API documentation will be available at http://localhost:9080/actuator/swagger-ui.

## Generating OpenAPI Specification

To generate the specification without starting the app, run:

```bash
./gradlew generateOpenApiDocs --no-configuration-cache
```

The generated file will be available in the [`./build`](./build/openapi.json) directory.

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file for details.
