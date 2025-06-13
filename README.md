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
- README [littlecode-setup](frameworks/spring/littlecode-setup/README.md)

### Quarkus - scheduled for version littlecode release/2.0.0
- README [littlecode-core](frameworks/quarkus/littlecode-core/README.md)
- README [littlecode-business](frameworks/quarkus/littlecode-business/README.md)
- README [littlecode-cloud-mq](frameworks/quarkus/littlecode-cloud-mq/README.md)
- README [littlecode-cloud-s3](frameworks/quarkus/littlecode-cloud-s3/README.md)
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

## Recomendations
- pom.xml

    - Package configuration
        - It is recommended to always include **core-spring** or **core-quarkus***
        ```xml
        <dependencies>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>core-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
        </dependencies>
        ``` 

        - Packages available 
        ```xml
        <properties>
            ...
            <littlecode-version>3.5.0</littlecode-version>
            ...
        </properties>
        ...
        <dependencies>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>core-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>setup-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>business-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>cloud-mq-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
            <dependency>
                <groupId>com.littlecode</groupId>
                <artifactId>cloud-s3-spring</artifactId>
                <version>${littlecode-version}</version>
            </dependency>
            ...
        </dependencies>
        ```
    - Otimization
      - To keep your application equivalent with the frameworks, replace the spring pom-parent with the littlecode one, each release simplifies the application of the package versions

      - This action is not mandatory, but it will help to maintain your application with equivalent and updated packages.

        spring boot **pom-parent**
        ```xml
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.3</version>
            <relativePath/>
        </parent>
        ...
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-dependencies</artifactId>
                    <version>3.2.3</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>3.2.3</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>

        </dependencyManagement>
        ```
        replace to:
        ```xml
        <parent>
            <groupId>com.littlecode</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.5.0</version>
            <relativePath/>
        </parent>
        ...
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>com.littlecode</groupId>
                    <artifactId>spring-boot-dependencies</artifactId>
                    <version>3.5.0</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <dependency>
                    <groupId>com.littlecode</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>3.5.0</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
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
