# Little code framework

Litlecode aims to reduce the verbosity of frameworks, thus providing classes and functions for more simplified use in applications, thus avoiding numerous and obvious classes of configurations and parameters

## Modules
- littlecode-core
- littlecode-business
- littlecode-cloud-mq
- littlecode-cloud-s3
- littlecode-dependencies-java
- littlecode-setup

### Spring
- README [littlecode-core](frameworks/spring/littlecode-core/README.md)
- README [littlecode-business](frameworks/spring/littlecode-business/README.md)
- README [littlecode-cloud-mq](frameworks/spring/littlecode-cloud-mq/README.md)
- README [littlecode-cloud-s3](frameworks/spring/littlecode-cloud-s3/README.md)
- README [littlecode-dependencies-java](frameworks/spring/littlecode-dependencies-java/README.md)
- README [littlecode-setup](frameworks/spring/littlecode-setup/README.md)

### Quarkus
- README [littlecode-core](frameworks/quarkus/littlecode-core/README.md)
- README [littlecode-business](frameworks/quarkus/littlecode-business/README.md)
- README [littlecode-cloud-mq](frameworks/quarkus/littlecode-cloud-mq/README.md)
- README [littlecode-cloud-s3](frameworks/quarkus/littlecode-cloud-s3/README.md)
- README [littlecode-dependencies-java](frameworks/quarkus/littlecode-dependencies-java/README.md)
- README [littlecode-setup](frameworks/quarkus/littlecode-setup/README.md)

### Prerequisites
- Spring
    - OS
        - [Debian](https://www.debian.org/distrib/) **The best**
        - [Ubuntu](https://ubuntu.com/) **The easy**
        - [Mint](https://www.linuxmint.com/) **The persistent**
        - [Windows-WSL2](https://learn.microsoft.com/pt-br/windows/wsl/install) **The ugly**
    - [JDK - 17](https://openjdk.org/install/)
    - [Maven - 3](https://maven.apache.org)  
    - [Spring Boot - [3.2.x]](https://spring.io/)
    - [Spring Cloud - [3.2.x]](https://spring.io/)
    - [Intellij](https://www.jetbrains.com/pt-br/idea/)

## Instalation
- Latest release
    ```bash
    git clone git@github.com:flaviomarcio/littlecode-sdk.git
    cd littlecode-sdk
    git checkout master
    ./mvn clean install
    ```
- Specific release/version 
    ```bash
    git clone git@github.com:flaviomarcio/littlecode-sdk.git
    cd littlecode-sdk
    git checkout release/1.0.0
    ./mvn clean install
    ```
- All release
    ```bash
    git clone git@github.com:flaviomarcio/littlecode-sdk.git
    cd littlecode-sdk
    git checkout release/1.0.0
    ./install
    ```

## Example and POCS
- go to the **./examples** folder
  - ./examples/spring
  - ./examples/quarkus


### Release specification
- Release 0.1.0
    - [JDK - 17](https://openjdk.org/install/)  
    - [Maven - 3](https://maven.apache.org)  
    - [Spring Boot - [3.2.2]](https://spring.io/)
    - [Spring Cloud - [3.2.2]](https://spring.io/)
- Release 1.2.0
    - [JDK - 17](https://openjdk.org/install/)  
    - [Maven - 3](https://maven.apache.org)  
    - [Spring Boot - [3.2.3]](https://spring.io/)
    - [Spring Cloud - [3.2.3]](https://spring.io/)

## New releases
New versions are always created when spring or quarkus framework versions are applied or new version of java
  - ex:
    - *spring* **3.2.2** release/0.0.0 
    - *spring* **3.2.3** release/1.0.0 
    - *java* **version 11** release/2.0.0 
    - *java* **version 17** release/3.0.0 
